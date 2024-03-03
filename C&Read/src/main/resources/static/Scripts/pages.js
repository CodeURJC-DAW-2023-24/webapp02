document.addEventListener('DOMContentLoaded', function () {
    var resultsContainer = document.getElementById('libraryList');
    var loadMoreBtn = document.getElementById('load-more-btn');
    var loadPrevBtn = document.getElementById('load-prev-btn');
    var spinner = document.getElementById('spinner');
    var page = 0;

    loadMoreBtn.addEventListener('click', function () {
        spinner.style.display = 'block';
        page++;
        fetch('/Library/Books/js?page=' + page, {
            method: 'GET',

        })
        .then(response => response.text())
        .then(data => {
            spinner.style.display = 'none';
            resultsContainer.innerHTML = data;
            
            
        })
        .catch(error => {
            console.error('Error al cargar más resultados:', error);
        });
    });

    loadPrevBtn.addEventListener('click', function () {
        spinner.style.display = 'block';
        page--;
        fetch('/Library/Books/js?page=' + page, {
            method: 'GET',

        })
        .then(response => response.text())
        .then(data => {
            spinner.style.display = 'none';
            resultsContainer.innerHTML = data;
            
        })
        .catch(error => {
            console.error('Error al cargar más resultados:', error);
        });
    });
});
