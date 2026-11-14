/** 格式化日期显示 */
export function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // 月份是从0开始的
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();

    // 使用padStart确保月份和日期始终是两位数
    const formattedMonth = String(month).padStart(2, '0');
    const formattedDay = String(day).padStart(2, '0');
    const formattedHours = String(hours).padStart(2, '0');
    const formattedMinutes = String(minutes).padStart(2, '0');

    return `${year}年${formattedMonth}月${formattedDay}日 ${formattedHours}:${formattedMinutes}`;
}

/** 格式化日期显示 */
export function formatDate2(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // 月份是从0开始的
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();

    // 使用padStart确保月份和日期始终是两位数
    const formattedMonth = String(month).padStart(2, '0');
    const formattedDay = String(day).padStart(2, '0');
    const formattedHours = String(hours).padStart(2, '0');
    const formattedMinutes = String(minutes).padStart(2, '0');

    return `${year}年${formattedMonth}月${formattedDay}日`;
}