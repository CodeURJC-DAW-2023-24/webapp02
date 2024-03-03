document.addEventListener('DOMContentLoaded', function () {
    var resultsContainer = document.getElementById('libraryList');
    var spinner = document.getElementById('spinner');
    var genreFilterBtn = document.getElementById('genreFilterBtn');

    var page = 0;
    var filterpage = 0;

    // Hacer algo con los datos del modelo
    console.log(contRoute);

    function loadResults(url) {
        spinner.style.display = 'block';

        fetch(url, {
            method: 'GET',
        })
        .then(response => response.text())
        .then(data => {
            spinner.style.display = 'none';
            resultsContainer.innerHTML = data;
            // Add the listeners after updating the content
            addEventListeners(); 
        })
        .catch(error => {
            console.error('Error al cargar más resultados:', error);
        });
    }

    function addEventListeners() {
        var loadPrevBtn = document.getElementById('load-prev-btn');
        var loadMoreBtn = document.getElementById('load-more-btn');

        if (loadPrevBtn) {
            loadPrevBtn.addEventListener('click', function () {
                page--;
                loadResults('/Library/' + contRoute + '/js?page=' + page);
            });
        }

        if (loadMoreBtn) {
            loadMoreBtn.addEventListener('click', function () {
                page++;
                loadResults('/Library/' + contRoute + '/js?page=' + page);
            });
        }

        if (genreFilterForm) {
            genreFilterForm.addEventListener('submit', function (event) {
                event.preventDefault(); // Evitar que el formulario se envíe normalmente
                page = 0; // Reiniciar la página al cambiar el filtro
                var selectedGenre = genreFilterForm.querySelector('select[name="genre"]').value;
                var genreUrl = '/Library/' + contRoute + '/Genre/js?page=0&genre=' + selectedGenre;
                console.log(genreUrl);
                loadResults(genreUrl);
            });
        }

        if (seasonFilterForm) {
            seasonFilterForm.addEventListener('submit', function (event) {
                event.preventDefault(); // Evitar que el formulario se envíe normalmente
                page = 0; // Reiniciar la página al cambiar el filtro
                var selectedSeason = seasonFilterForm.querySelector('select[name="season"]').value;
                var seasonUrl = '/Library/' + contRoute + '/Season/js?page=0&season=' + selectedSeason;
                console.log(seasonUrl);
                loadResults(seasonUrl);
            });
        }

        if (countryFilterForm) {
            countryFilterForm.addEventListener('submit', function (event) {
                event.preventDefault(); // Evitar que el formulario se envíe normalmente
                page = 0; // Reiniciar la página al cambiar el filtro
                var selectedCountry = countryFilterForm.querySelector('select[name="country"]').value;
                var countryUrl = '/Library/' + contRoute + '/Country/js?page=0&country=' + selectedCountry;
                console.log(countryUrl);
                loadResults(countryUrl);
            });
        }

        if (stateFilterForm) {
            stateFilterForm.addEventListener('submit', function (event) {
                event.preventDefault(); // Evitar que el formulario se envíe normalmente
                page = 0; // Reiniciar la página al cambiar el filtro
                var selectedState = stateFilterForm.querySelector('select[name="state"]').value;
                var stateUrl = '/Library/' + contRoute + '/State/js?page=0&state=' + selectedState;
                console.log(stateUrl);
                loadResults(stateUrl);
            });
        }
    }

    // Initially it adds the Listeners
    addEventListeners();
});
