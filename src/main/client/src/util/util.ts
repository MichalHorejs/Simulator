/**
 * Generates timestamp in java format. For example: '2025-04-25T18:19:54.136Z'
 */
function generateLocalTimestamp(): string {
    const now = new Date();
    const offsetMs = now.getTimezoneOffset() * 60000; // rozdíl v milisekundách
    const localTime = new Date(now.getTime() - offsetMs);
    return localTime.toISOString().slice(0, -1);
}


export default generateLocalTimestamp;