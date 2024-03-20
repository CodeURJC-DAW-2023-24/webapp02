document.addEventListener('DOMContentLoaded', function () {

    console.log("ma", elementsMap);

    // Obtener el elemento donde quieres agregar el contenido
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
    const topTitleUser = document.getElementById("topTitleUser");
    const topListRow = document.querySelector('#topList .row');


    // iteramos sobre cada key
    map.forEach((value, key) => {
        const article = document.createElement('article');
        article.id = 'topSection'; // Assuming you want to give each article the same ID, you may want to use different IDs if needed
        const topTitleUser = document.createElement('div');
        topTitleUser.id = 'topTitleUser';
        topTitleUser.innerHTML = `<h4>${key}</h4>`;
        article.appendChild(topTitleUser);

        const topList = document.createElement('div');
        topList.id = 'topList';
        topList.classList.add('container');
        const topListRow = document.createElement('div');
        topListRow.classList.add('row');

        value.forEach(element => {
            console.log(`Título: ${element.name}, Autor: ${element.author}`); // Usar element.title y element.author
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

        // Add the article to the container element (assuming you have a container element)
        container.appendChild(article);
    });
});