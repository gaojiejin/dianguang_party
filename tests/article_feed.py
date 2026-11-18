"""Read-only checks for the local homepage pagination endpoint."""
import json
from urllib.request import urlopen
from urllib.parse import urlencode
from urllib.error import HTTPError

def query(**params):
    with urlopen('http://127.0.0.1:8080/article/passed-page?'+urlencode(params),timeout=20) as response:
        return json.load(response)

for category in ['时政要闻','支部动态','微课堂展示']:
    first=query(type=category,page=1)
    assert first['pageSize']==5 and len(first['records'])<=5
    assert all(row['reviewState']==1 for row in first['records'])
    assert all(row['type']==category or category=='微课堂展示' and row['type']=='链接展示' for row in first['records'])
    ids=[row['articleId'] for row in first['records']]
    if first['total']>5:
        second=query(type=category,page=2)
        assert not set(ids)&{row['articleId'] for row in second['records']}
    if first['records']:
        title=first['records'][0]['title']
        result=query(type=category,keyword=title)
        assert result['total']>0 and all(title in row['title'] for row in result['records'])
    empty=query(type=category,keyword='dg_missing_9ba31ac4')
    assert empty['total']==0 and empty['records']==[]
    clamped=query(type=category,page=2147483647)
    assert clamped['page']==max(1,(clamped['total']+4)//5)
    print('PASS:',category,'pagination, title search, empty results, page bounds')
for params in [dict(type='invalid'),dict(type='支部动态',page=0),dict(type='支部动态',keyword='x'*101)]:
    try:
        query(**params)
        raise AssertionError('expected HTTP 400')
    except HTTPError as error:
        assert error.code==400
print('PASS: input validation')
