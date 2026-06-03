// 创建遮罩层
const overlay = document.createElement('div');
overlay.className = 'overlay';
document.body.appendChild(overlay);

let currentPage = 0;
const pageSize = 5;

// 使用 addEventListener 避免覆盖其他 onload 事件
window.addEventListener('load', function() {
    // 为新增按钮添加点击事件
    const addButton = document.getElementById('addButton');
    if (addButton) {
        addButton.addEventListener('click', function() {
            const websiteForm = document.getElementById('websiteForm');
            if (websiteForm) {
                websiteForm.style.display = 'block';
            }
            overlay.style.display = 'block';
        });
    }

    const websiteForm = document.getElementById('websiteForm');
    if (websiteForm) {
        websiteForm.addEventListener('submit', function(event) {
            event.preventDefault();
            submitWebsiteForm();
            websiteForm.style.display = 'none';
            overlay.style.display = 'none';
        });
    }

    const editForm = document.getElementById('editForm');
    if (editForm) {
        editForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const id = document.getElementById('editId').value;
            const name = document.getElementById('editName').value;
            const url = document.getElementById('editUrl').value;
            const alexa = parseInt(document.getElementById('editAlexa').value);
            const country = document.getElementById('editCountry').value;

            const websiteData = {
                name: name,
                url: url,
                alexa: alexa,
                country: country
            };

            fetchRequest(`http://localhost:8080/website/${id}`, 'PUT', websiteData)
                .then(data => {
                    getWebsiteList();
                    editForm.style.display = 'none';
                    overlay.style.display = 'none';
                    const result = document.getElementById('result');
                    if (result) {
                        result.innerText = '网站编辑成功: ' + JSON.stringify(data);
                    }
                })
                .catch(error => {
                    const result = document.getElementById('result');
                    if (result) {
                        result.innerText = '网站编辑失败: ' + error;
                    }
                });
        });
    }
});

// 统一请求方法，自动携带Token
function fetchRequest(url, method, data) {
    const headers = {
        'Content-Type': 'application/json'
    };

    // 从localStorage获取Token并加入请求头
    const token = localStorage.getItem('token');
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    return fetch(url, {
        method: method,
        headers: headers,
        body: data ? JSON.stringify(data) : null
    })
        .then(response => {
            // 处理401未授权（Token过期或无效）
            if (response.status === 401) {
                alert('登录已过期，请重新登录');
                logout();
                throw new Error('Unauthorized');
            }
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        });
}

function submitWebsiteForm() {
    const websiteForm = document.getElementById('websiteForm');
    if (websiteForm) {
        const formData = new FormData(websiteForm);
        const websiteData = {
            name: formData.get('name'),
            url: formData.get('url'),
            alexa: parseInt(formData.get('alexa')),
            country: formData.get('country')
        };

        fetchRequest('http://localhost:8080/website', 'POST', websiteData)
            .then(data => {
                getWebsiteList();
                const result = document.getElementById('result');
                if (result) {
                    result.innerText = '网站添加成功: ' + JSON.stringify(data);
                }
            })
            .catch(error => {
                if (error.message !== 'Unauthorized') {
                    const result = document.getElementById('result');
                    if (result) {
                        result.innerText = '网站添加失败: ' + error;
                    }
                }
            });
    }
}

function getWebsiteList(name = '', country = '', alexa = '') {
    let url = `http://localhost:8080/websites?page=${currentPage}&size=${pageSize}`;
    if (name) url += `&name=${encodeURIComponent(name)}`;
    if (country) url += `&country=${encodeURIComponent(country)}`;
    if (alexa) url += `&alexa=${alexa}`;

    fetchRequest(url, 'GET', null)
        .then(bar => {
            let tableRows = '';
            if (bar.data && bar.data.length > 0) {
                bar.data.forEach(item => {
                    const itemName = item.name || '无名网站';
                    const itemCountry = item.country || '未知';
                    const itemUrl = (item.url || '').replace(/'/g, "\\'");
                    const editParams = `${item.id}, '${itemName}', '${itemUrl}', ${item.alexa}, '${itemCountry}'`;
                    tableRows += `
                        <tr>
                            <td>${itemName}</td>
                            <td><a href="${item.url}" target="_blank">${item.url}</a></td>
                            <td>${item.alexa}</td>
                            <td>${itemCountry}</td>
                            <td>
                                <button onclick="deleteWebsite(${item.id})">删除</button>
                                <button onclick="editWebsite(${editParams})">编辑</button>
                            </td>
                        </tr>
                    `;
                });
            } else {
                // 无数据时显示提示行
                tableRows = `<tr><td colspan="5" style="padding:30px;color:#999;text-align:center;">暂无数据，点击"新增网站"添加</td></tr>`;
            }

            const tableBody = document.getElementById('websiteTableBody');
            if (tableBody) {
                tableBody.innerHTML = tableRows;
            }
            const pageInfoEl = document.getElementById('pageInfo');
            if (pageInfoEl) {
                pageInfoEl.innerText = `第 ${currentPage + 1} 页`;
            }
        })
        .catch(error => {
            if (error.message !== 'Unauthorized') {
                console.error('Error:', error);
                const tableBody = document.getElementById('websiteTableBody');
                if (tableBody) {
                    tableBody.innerHTML = `<tr><td colspan="5" style="padding:30px;color:#ff6b6b;text-align:center;">数据加载失败: ${error.message}</td></tr>`;
                }
            }
        });
}

function searchWebsites() {
    const name = document.getElementById('nameSearch') ? document.getElementById('nameSearch').value : '';
    const country = document.getElementById('countrySearch') ? document.getElementById('countrySearch').value : '';
    const alexaInput = document.getElementById('alexaSearch');
    const alexa = alexaInput ? alexaInput.value.trim() : '';
    currentPage = 0;
    getWebsiteList(name, country, alexa);
}

function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        searchWebsites();
    }
}

function nextPage() {
    currentPage++;
    searchWebsites();
}

function deleteWebsite(id) {
    if (confirm('确定要删除这个网站吗？')) {
        fetchRequest(`http://localhost:8080/website/${id}`, 'DELETE', null)
            .then(data => {
                getWebsiteList();
                const result = document.getElementById('result');
                if (result) {
                    result.innerText = '网站删除成功: ' + JSON.stringify(data);
                }
            })
            .catch(error => {
                if (error.message !== 'Unauthorized') {
                    const result = document.getElementById('result');
                    if (result) {
                        result.innerText = '网站删除失败: ' + error;
                    }
                }
            });
    }
}

function editWebsite(id, name, url, alexa, country) {
    const editForm = document.getElementById('editForm');
    if (editForm) {
        const editId = document.getElementById('editId');
        const editName = document.getElementById('editName');
        const editUrl = document.getElementById('editUrl');
        const editAlexa = document.getElementById('editAlexa');
        const editCountry = document.getElementById('editCountry');

        if (editId) editId.value = id;
        if (editName) editName.value = name;
        if (editUrl) editUrl.value = url;
        if (editAlexa) editAlexa.value = alexa;
        if (editCountry) editCountry.value = country;

        editForm.style.display = 'block';
        overlay.style.display = 'block';
    }
}

function closeAddForm() {
    const websiteForm = document.getElementById('websiteForm');
    if (websiteForm) {
        websiteForm.style.display = 'none';
    }
    overlay.style.display = 'none';
}

function closeEditForm() {
    const editForm = document.getElementById('editForm');
    if (editForm) {
        editForm.style.display = 'none';
    }
    overlay.style.display = 'none';
}

function clearAllInputs() {
    const el = (id) => document.getElementById(id);
    if (el('nameSearch')) el('nameSearch').value = '';
    if (el('countrySearch')) el('countrySearch').value = '';
    if (el('alexaSearch')) el('alexaSearch').value = '';

    if (el('name')) el('name').value = '';
    if (el('url')) el('url').value = '';
    if (el('alexa')) el('alexa').value = '';
    if (el('country')) el('country').value = '';

    if (el('editName')) el('editName').value = '';
    if (el('editUrl')) el('editUrl').value = '';
    if (el('editAlexa')) el('editAlexa').value = '';
    if (el('editCountry')) el('editCountry').value = '';
    if (el('editId')) el('editId').value = '';
}

function exportWebsites() {
    const token = localStorage.getItem('token');
    const apiUrl = 'http://localhost:8080/export/websites';

    const headers = {};
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    fetch(apiUrl, { headers: headers })
        .then(response => {
            if (response.status === 401) {
                alert('登录已过期，请重新登录');
                logout();
                throw new Error('Unauthorized');
            }
            if (!response.ok) {
                throw new Error('网络响应异常');
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'websites.csv';
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            if (error.message !== 'Unauthorized') {
                console.error('导出失败:', error);
                alert('导出失败，请稍后重试');
            }
        });
}
