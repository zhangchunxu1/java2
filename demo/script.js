// 创建遮罩层
const overlay = document.createElement('div');
overlay.className = 'overlay';
document.body.appendChild(overlay);
let currentPage = 0;
const pageSize = 5;
window.onload = function() {
    getWebsiteList();
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
            // 提交后隐藏表单和遮罩层
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
                const result = document.getElementById('result');
                if (result) {
                    result.innerText = '网站添加失败: ' + error;
                }
            });
    }
}

function fetchRequest(url, method, data) {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: data ? JSON.stringify(data) : null
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        });
}

function getWebsiteList(name = '', country = '', alexa = '') {
    let url = `http://localhost:8080/websites?page=${currentPage}&size=${pageSize}`;
    if (name) url += `&name=${name}`;
    if (country) url += `&country=${country}`;
    if (alexa) url += `&alexa=${alexa}`;

    fetchRequest(url, 'GET', null)
        .then(bar => {
            let tableRows = '';
            if (bar.data) {
                bar.data.forEach(item => {
                    const name = item.name || '无名网站';
                    const country = item.country || '未知';
                    tableRows += `
                        <tr>
                            <td>${name}</td>
                            <td><a href="${item.url}" target="_blank">${item.url}</a></td>
                            <td>${item.alexa}</td>
                            <td>${country}</td>
                            <td>
                                <button onclick="deleteWebsite(${item.id})">删除</button>
                                <button onclick="editWebsite(${item.id}, '${item.name}', '${item.url}', ${item.alexa}, '${item.country}')">编辑</button>
                            </td>
                        </tr>
                    `;
                });
            }
            // 获取 tbody 元素并更新内容
            const tableBody = document.getElementById('websiteTableBody');
            if (tableBody) {
                tableBody.innerHTML = tableRows;
            }
            document.getElementById('pageInfo').innerText = `第 ${currentPage + 1} 页`;
        })
        .catch(error => console.error('Error:', error));
}

function searchWebsites() {
    const name = document.getElementById('nameSearch').value;
    const country = document.getElementById('countrySearch').value;
    const alexaInput = document.getElementById('alexaSearch');
    const alexa = alexaInput.value.trim();
    currentPage = 0;
    getWebsiteList(name, country, alexa);
}

function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        const name = document.getElementById('nameSearch').value;
        const country = document.getElementById('countrySearch').value;
        const alexaInput = document.getElementById('alexaSearch');
        const alexa = alexaInput.value.trim();
        getWebsiteList(name, country, alexa);
    }
}

function nextPage() {
    currentPage++;
    const name = document.getElementById('nameSearch').value;
    const country = document.getElementById('countrySearch').value;
    const alexaInput = document.getElementById('alexaSearch');
    const alexa = alexaInput.value.trim();
    getWebsiteList(name, country, alexa);
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
                const result = document.getElementById('result');
                if (result) {
                    result.innerText = '网站删除失败: ' + error;
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
    // 清空搜索框
    document.getElementById('nameSearch').value = '';
    document.getElementById('countrySearch').value = '';
    document.getElementById('alexaSearch').value = '';

    // 清空新增表单
    document.getElementById('name').value = '';
    document.getElementById('url').value = '';
    document.getElementById('alexa').value = '';
    document.getElementById('country').value = '';

    // 清空编辑表单
    document.getElementById('editName').value = '';
    document.getElementById('editUrl').value = '';
    document.getElementById('editAlexa').value = '';
    document.getElementById('editCountry').value = '';
    document.getElementById('editId').value = '';
}