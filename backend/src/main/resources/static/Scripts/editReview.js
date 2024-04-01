document.addEventListener('DOMContentLoaded', function () {

    var resultsContainer = document.getElementById('modalEditReview');

    function addEventListeners() {

        var modifyForm = document.getElementById('editReviewForm');
        console.log("modifyform:");
        console.log(modifyForm);
        var reviewid = document.getElementById('inputrevid');
        var savedID = document.getElementById('reviewID').getAttribute('data-revid');
        console.log(savedID);

        reviewid.value = savedID;
        console.log(reviewid);

        if (modifyForm){
            modifyForm.addEventListener('submit2', function (event){
                event.preventDefault();

                var modelData = new FormData(modifyForm);
                var editUrl = '/review/edit/add';
                fetch(editUrl, {
                    method: 'POST',
                    body: modelData
                })
                .then(response => response.text())
                .then(data => {
                    resultsContainer.innerHTML = data;
                    addEventListeners(); 
                })
                .catch(error => {
                    console.error('ERROR JAVASCRIPT REVIEW', error);    
                });

            });
        };

    }
    addEventListeners();
});