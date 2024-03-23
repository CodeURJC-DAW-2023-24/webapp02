// Function to obtain the details of a book using its ID
const getBookDetailsFromOpenLibrary = (bookId) => {
    //I have to see if it is posible to do all the CRUD operations
    const apiUrl = `https://openlibrary.org/api/books/${bookId}.json`;

    fetch(apiUrl)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error(`Error al obtener los detalles del libro: ${response.status} - ${response.statusText}`);
            }
        })
        .then(book => {
            // Sending the data to my controller
            sendBookDetailsToController(book);
        })
        .catch(error => {
            console.error('Error al obtener los detalles del libro:', error);
        });
};

// Function to send the data of a book to my controller
const sendBookDetailsToController = (bookDetails) => {
    const method = 'POST'; 
    
    fetch('/books', {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bookDetails)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error al enviar los detalles del libro al controlador: ${response.status} - ${response.statusText}`);
        }
    })
    .catch(error => {
        console.error('Error al enviar los detalles del libro al controlador:', error);
    });
};

