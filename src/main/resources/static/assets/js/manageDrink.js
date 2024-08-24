document.addEventListener('DOMContentLoaded', function () {
    fetchDrinks();

    document.getElementById('drinkForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const id = document.getElementById('drinkId').value;
        const name = document.getElementById('name').value.trim();
        const price = parseFloat(document.getElementById('price').value);
        const capacity = parseInt(document.getElementById('capacity').value, 10);

        if (price < 0) {
            alert('Price cannot be negative.');
            return;
        }

        const drink = { name, price, capacity };

        if (id) {
            updateDrink(id, drink);
        } else {
            manageDrink(drink);
        }
    });
});

function fetchDrinks() {
    fetch('/drink')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch drinks.');
            }
            return response.json();
        })
        .then(drinks => {
            displayDrinks(drinks);
        })
        .catch(error => {
            console.error('Error fetching drinks:', error);
            alert('Failed to fetch drinks.');
        });
}

function displayDrinks(drinks) {
    const drinksTableBody = document.getElementById('drinksTableBody');
    drinksTableBody.innerHTML = '';

    drinks.forEach(drink => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${drink.name}</td>
            <td>${drink.price.toFixed(2)}</td>
            <td>${drink.capacity}</td>
            <td>
                <button onclick="editDrink(${drink.idDrink})">Edit</button>
                <button onclick="deleteDrink(${drink.idDrink})">Delete</button>
            </td>
        `;
        drinksTableBody.appendChild(row);
    });
}

function manageDrink(drink) {
    fetch('/drink', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(drink),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to add drink.');
            }
            return response.json();
        })
        .then(() => {
            fetchDrinks();
            resetForm();
        })
        .catch(error => {
            console.error('Error adding drink:', error);
            alert('Failed to add drink.');
        });
}

function editDrink(id) {
    fetch(`/drink/idDrink/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch drink.');
            }
            return response.json();
        })
        .then(drink => {
            document.getElementById('drinkId').value = drink.idDrink;
            document.getElementById('name').value = drink.name;
            document.getElementById('price').value = drink.price;
            document.getElementById('capacity').value = drink.capacity;
            document.getElementById('submitButton').textContent = 'Update Drink';
        })
        .catch(error => {
            console.error('Error fetching drink:', error);
            alert('Failed to fetch drink.');
        });
}

function updateDrink(id, drink) {
    fetch(`/drink/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(drink),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update drink.');
            }
            return response.json();
        })
        .then(() => {
            fetchDrinks();
            resetForm();
        })
        .catch(error => {
            console.error('Error updating drink:', error);
            alert('Failed to update drink.');
        });
}

function deleteDrink(id) {
    if (confirm('Are you sure you want to delete this drink?')) {
        fetch(`/drink/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete drink.');
                }
                fetchDrinks();
            })
            .catch(error => {
                console.error('Error deleting drink:', error);
                alert('Failed to delete drink.');
            });
    }
}

function resetForm() {
    document.getElementById('drinkId').value = '';
    document.getElementById('name').value = '';
    document.getElementById('price').value = '';
    document.getElementById('capacity').value = '';
    document.getElementById('submitButton').textContent = 'Add Drink';
}