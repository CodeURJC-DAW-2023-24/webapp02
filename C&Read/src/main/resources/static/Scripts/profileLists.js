document.addEventListener('DOMContentLoaded', function () {
    console.log("ma", elementsMap);

    function decodeHTMLEntities(elementsMap) {
        var textarea = document.createElement('textarea');
        textarea.innerHTML = elementsMap;
        return textarea.value;
    }

    var decodedJsonString = decodeHTMLEntities(elementsMap);
    console.log("ma4", decodedJsonString);

    const map = new Map(Object.entries(JSON.parse(decodedJsonString)));
    console.log("ma2", map);

    const container = document.getElementById('tops');

    map.forEach((value, key) => {
        const article = document.createElement('article');
        article.id = 'topSection';

        const articleHeader = document.createElement('div');
        articleHeader.id = 'articleHeader';

        const topTitleUser = document.createElement('div');
        topTitleUser.id = 'topTitleUser';
        topTitleUser.innerHTML = `<h4>${key}</h4>`;
        articleHeader.appendChild(topTitleUser);

        const modalButton = document.createElement('button');
        modalButton.type = 'button';
        modalButton.classList.add('btn', 'btn-primary');
        modalButton.setAttribute('data-bs-toggle', 'modal');
        modalButton.setAttribute('data-bs-target', `#deleteModal`);
        modalButton.textContent = 'Eliminar Elemento';
        articleHeader.appendChild(modalButton);
        article.appendChild(articleHeader);

        const topList = document.createElement('div');
        topList.id = 'topList';
        topList.classList.add('container');
        const topListRow = document.createElement('div');
        topListRow.classList.add('row');

        value.forEach(element => {
            console.log(`Título: ${element.name}, Autor: ${element.author}`);
            const wCardHTML = `
                <div class="col-sm-4">
                    <a href="/SingleElement/${element.id}" class="card-link">
                        <div class="card text-bg-dark">
                            <img src="data:image/jpg;base64, ${element.base64Image}" class="card-img-top" alt="Imagen">
                            <div class="card-img-overlay">
                                <div id="cardContent" class="mt-auto position-absolute bottom-0 start-0 end-0 bg-black opacity-75 p-2">
                                    <h4 class="card-title">${element.name}</h4>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            `;
            topListRow.insertAdjacentHTML('beforeend', wCardHTML);
        });
        topList.appendChild(topListRow);
        article.appendChild(topList);

        let modalOptions = '';

        value.forEach(element => {
            modalOptions += `<option value="${key}/${element.id}">${element.name}</option>`;
        });

        const modal = `
            <div class="modal" id="deleteModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form id="deleteFromUserList" action="/users/updateLists" method="post" enctype="multipart/form-data">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="deleteModalLabel">Eliminar elemento de la lista</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <select id="listSelect" name="listId" class="form-select" aria-label="Eliminar de la lista:">
                                    ${modalOptions}
                                </select>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary">Eliminar</button>
                            </div>
                            <input type="hidden" name="_csrf" value="${token}" />
                        </form>
                    </div>
                </div>
            </div>
        `;

        container.insertAdjacentHTML('beforeend', modal);
        container.appendChild(article);
    });
});
