document.addEventListener('DOMContentLoaded', function () {

    var resultsContainer = document.getElementById('modifyBookForm');


    function loadResults(url) {

        fetch(url, {
            method: 'POST',
        })
        .then(response => response.text())
        .then(data => {
            resultsContainer.innerHTML = data;
            addEventListeners(); 
            setDefaultSelection(); 
        })
        .catch(error => {
            console.error('Error al cargar más resultados:', error);
        });
    }

    function addEventListeners() {

        var modifyForm = document.getElementById('modifyForm');
        var typeForm = document.getElementById('typeForm');
        if (modifyForm) {
            modifyForm.addEventListener('submit', function (event) {
                event.preventDefault(); 
                page = 0; 
                var formData = new FormData(modifyForm);
                var editUrl = '/SingleElement/edit';
                fetch(editUrl, {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.text())
                .then(data => {
                    resultsContainer.innerHTML = data;
                    addEventListeners(); 
                    setDefaultSelection(); // Llamar a la función para establecer la selección por defecto
                })
                .catch(error => {
                    console.error('Error al cargar más resultados:', error);
                });
            });
        }

        if (typeForm) {
            typeForm.addEventListener('submit', function (event) {
                event.preventDefault(); 
                page = 0; 
                var formData = new FormData(typeForm);
                var editUrl = '/SingleElement/edit/type';
                fetch(editUrl, {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.text())
                .then(data => {
                    resultsContainer.innerHTML = data;
                    addEventListeners(); 
                    setDefaultSelection(); // Llamar a la función para establecer la selección por defecto
                })
                .catch(error => {
                    console.error('Error al cargar más resultados:', error);
                });
            });
        }

    }


    function setDefaultSelection() {
        // Obtener el valor de la estación del elemento desde el atributo de datos personalizados
        var seasonValueFromElement = document.getElementById('elementSeason').getAttribute('data-season');
        var typeValueFromElement = document.getElementById('elementType').getAttribute('data-type');
        var stateValueFromElement = document.getElementById('elementState').getAttribute('data-state');
        var genresValueFromElement = document.getElementById('elementGenre').getAttribute('data-genre');



        
        var seasonSelect = document.getElementById('season-select'); 
        var typeSelect = document.getElementById('type-select'); 
        var sateSelect = document.getElementById('state-select'); 
        var genresSelect = document.getElementById('genres-select'); 

        if (seasonSelect) {
            // Iterar sobre las opciones del campo de selección
            for (var i = 0; i < seasonSelect.options.length; i++) {
                // Verificar si el valor de la opción coincide con el valor de la estación del elemento
                if (seasonSelect.options[i].value === seasonValueFromElement) {
                    // Establecer la opción como seleccionada
                    seasonSelect.options[i].selected = true;
                    break; // Salir del bucle una vez que se haya establecido la selección
                }
            }
        }

        if (typeSelect) {
            // Iterar sobre las opciones del campo de selección
            for (var i = 0; i < typeSelect.options.length; i++) {
                // Verificar si el valor de la opción coincide con el valor de la estación del elemento
                if (typeSelect.options[i].value === typeValueFromElement) {
                    // Establecer la opción como seleccionada
                    typeSelect.options[i].selected = true;
                    break; // Salir del bucle una vez que se haya establecido la selección
                }
            }
        }

        if (sateSelect) {
            // Iterar sobre las opciones del campo de selección
            for (var i = 0; i < sateSelect.options.length; i++) {
                // Verificar si el valor de la opción coincide con el valor de la estación del elemento
                if (sateSelect.options[i].value === stateValueFromElement) {
                    // Establecer la opción como seleccionada
                    sateSelect.options[i].selected = true;
                    break; // Salir del bucle una vez que se haya establecido la selección
                }
            }
        }


        if (genresSelect) {
            console.log(genresValueFromElement);
            // Iterar sobre las opciones del campo de selección
            for (var i = 0; i < genresSelect.options.length; i++) {
                
                // Verificar si el valor de la opción coincide con el valor de la estación del elemento
                if ( genresValueFromElement.includes(genresSelect.options[i].value)) {
                    // Establecer la opción como seleccionada
                    genresSelect.options[i].selected = true;
                }
            }
        }





    }

    addEventListeners();
});
