"""Check persistence across restart using an isolated disposable fixture."""
import json
import os
import subprocess
import sys
import urllib.parse
import urllib.request
import uuid
from pathlib import Path

state = Path('.local/restart-learning-test.json')
state.parent.mkdir(exist_ok=True)

def sql(statement):
    env = dict(os.environ, MYSQL_PWD=os.environ.get('DB_PASSWORD', 'root'))
    return subprocess.run([
        r'D:/mysql/install/MySQL Server 8.0/bin/mysql.exe', '--user=root',
        '--database=dianguang_party', '--batch', '--skip-column-names'
    ], input=statement, encoding='utf-8', capture_output=True, check=True,
       env=env).stdout.strip()

def report(data):
    req = urllib.request.Request(
        'http://127.0.0.1:8080/classLearnInfo/addLearnDuration',
        data=urllib.parse.urlencode(data).encode())
    return urllib.request.urlopen(req, timeout=30).read()

if sys.argv[1] == 'prepare':
    job = 'dg_restart_' + uuid.uuid4().hex[:10]
    sql("INSERT INTO user(job_id,password,name,role) VALUES('%s','test','restart-test','党员');" % job)
    user = int(sql("SELECT id FROM user WHERE job_id='%s';" % job))
    course, chapter = map(int, sql('SELECT class_id,chapter_id FROM chapter ORDER BY chapter_id DESC LIMIT 1;').split('\t'))
    data = dict(user_id=user, class_id=course, chapter_id=chapter,
                if_learned=0, learn_duration=11,
                report_id='restart_' + uuid.uuid4().hex)
    state.write_text(json.dumps(data))
    report(data)
    print('Prepared disposable report for restart verification')
else:
    data = json.loads(state.read_text())
    user = data['user_id']
    try:
        report(data)
        duration = int(sql('SELECT learning_duration FROM learn_rate WHERE user_id=%s AND chapter_id=%s;' % (user, data['chapter_id'])))
        assert duration == 11, duration
        print('PASS: backend restart does not duplicate the learning report')
    finally:
        for table in ['learning_report', 'learn_rate', 'class_learn_info']:
            sql('DELETE FROM %s WHERE user_id=%s;' % (table, user))
        sql('DELETE FROM user WHERE id=%s;' % user)
        state.unlink()
