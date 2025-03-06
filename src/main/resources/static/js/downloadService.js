/**
 * 使用XMLHttpRequest下载文件
 * @param {string} url - 下载地址
 * @param {Object} params - URL参数
 * @returns {Promise} - 返回Promise对象
 */
function downloadWithXHR(url, params = {}) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        const queryString = new URLSearchParams(params).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;

        xhr.open('GET', fullUrl, true);
        xhr.responseType = 'blob';

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const contentType = xhr.getResponseHeader('Content-Type');
                    const blob = new Blob([xhr.response], {
                        type: contentType || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                    });
                    resolve(blob);
                } else if (xhr.status === 500) {
                    const reader = new FileReader();
                    reader.onload = function() {
                        try {
                            const error = JSON.parse(reader.result);
                            reject(new Error(error.message || '下载失败'));
                        } catch (e) {
                            reject(new Error('下载过程中发生错误，请稍后重试'));
                        }
                    };
                    reader.readAsText(xhr.response);
                } else {
                    reject(new Error('下载过程中发生错误，请稍后重试'));
                }
            }
        };

        xhr.onerror = function() {
            reject(new Error('网络错误，请稍后重试'));
        };

        xhr.send();
    });
}

/**
 * 使用XMLHttpRequest POST方式下载文件
 * @param {string} url - 下载地址
 * @param {Object} data - POST数据
 * @returns {Promise} - 返回Promise对象
 */
function downloadWithXHRPost(url, data = {}) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.responseType = 'blob';
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const contentType = xhr.getResponseHeader('Content-Type');
                    const blob = new Blob([xhr.response], {
                        type: contentType || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                    });
                    resolve(blob);
                } else if (xhr.status === 500) {
                    const reader = new FileReader();
                    reader.onload = function() {
                        try {
                            const error = JSON.parse(reader.result);
                            reject(new Error(error.message || '下载失败'));
                        } catch (e) {
                            reject(new Error('下载过程中发生错误，请稍后重试'));
                        }
                    };
                    reader.readAsText(xhr.response);
                } else {
                    reject(new Error('下载过程中发生错误，请稍后重试'));
                }
            }
        };

        xhr.onerror = function() {
            reject(new Error('网络错误，请稍后重试'));
        };

        xhr.send(JSON.stringify(data));
    });
}

/**
 * 使用Fetch API下载文件
 * @param {string} url - 下载地址
 * @param {Object} params - URL参数
 * @returns {Promise} - 返回Promise对象
 */
function downloadWithFetch(url, params = {}) {
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${url}?${queryString}` : url;

    return fetch(fullUrl)
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(error.message || '下载失败');
                });
            }
            return response.blob();
        })
        .then(blob => {
            const contentType = blob.type || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            return new Blob([blob], { type: contentType });
        });
}

/**
 * 使用Fetch API POST方式下载文件
 * @param {string} url - 下载地址
 * @param {Object} data - POST数据
 * @returns {Promise} - 返回Promise对象
 */
function downloadWithFetchPost(url, data = {}) {
    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(error => {
                throw new Error(error.message || '下载失败');
            });
        }
        return response.blob();
    })
    .then(blob => {
        const contentType = blob.type || 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
        return new Blob([blob], { type: contentType });
    });
}

/**
 * 触发文件下载
 * @param {Blob} blob - 文件内容
 * @param {string} filename - 文件名
 */
function triggerDownload(blob, filename) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
}

/**
 * 测试downloadWithXHRPost的使用方法
 * @param {string} url - 下载地址
 * @param {Object} data - 请求参数
 * @param {string} filename - 保存的文件名
 */
function test(url, data = {}, filename = 'download.xlsx') {
    downloadWithXHRPost(url, data)
        .then(blob => {
            // 使用triggerDownload触发文件下载
            triggerDownload(blob, filename);
        })
        .catch(error => {
            // 处理下载过程中的错误
            console.error('下载失败:', error.message);
            // 可以在这里添加其他错误处理逻辑，比如显示错误提示等
        });
}

export { downloadWithXHR, downloadWithXHRPost, downloadWithFetch, downloadWithFetchPost, triggerDownload, test };