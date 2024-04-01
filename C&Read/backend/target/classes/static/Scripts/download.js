document.addEventListener('DOMContentLoaded', function () {
    var downloadNamesBtn = document.getElementById('downloadNamesBtn');

    if (downloadNamesBtn) {
        downloadNamesBtn.addEventListener('click', function () {
            // Actualiza la URL para apuntar al nuevo endpoint
            window.location.href = '/downloadNames';
        });
    }
});