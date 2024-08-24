document.addEventListener('DOMContentLoaded', function () {
    fetchFoods();

    document.getElementById('foodForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const id = document.getElementById('foodId').value;
        const name = document.getElementById('name').value.trim();
        const price = parseFloat(document.getElementById('price').value);
        const size = document.getElementById('size').value.trim();

        if (price < 0) {
            alert('Price cannot be negative.');
            return;
        }

        const food = { name, price, size };

        if (id) {
            updateFood(id, food);
        } else {
            addFood(food);
        }
    });
});

function fetchFoods() {
    fetch('/food')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch foods.');
            }
            return response.json();
        })
        .then(foods => {
            displayFoods(foods);
        })
        .catch(error => {
            console.error('Error fetching foods:', error);
            alert('Failed to fetch foods.');
        });
}

function displayFoods(foods) {
    const foodTableBody = document.getElementById('foodTableBody');
    foodTableBody.innerHTML = '';

    foods.forEach(food => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${food.name}</td>
            <td>${food.price.toFixed(2)}</td>
            <td>${food.size}</td>
            <td>
                <button onclick="editFood(${food.idFood})">Edit</button>
                <button onclick="deleteFood(${food.idFood})">Delete</button>
            </td>
        `;
        foodTableBody.appendChild(row);
    });
}

function addFood(food) {
    fetch('/food', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(food),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to add food.');
            }
            return response.json();
        })
        .then(() => {
            fetchFoods();
            resetForm();
        })
        .catch(error => {
            console.error('Error adding food:', error);
            alert('Failed to add food.');
        });
}

function editFood(id) {
    fetch(`/food/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch food.');
            }
            return response.json();
        })
        .then(food => {
            document.getElementById('foodId').value = food.idFood;
            document.getElementById('name').value = food.name;
            document.getElementById('price').value = food.price;
            document.getElementById('size').value = food.size;
            document.getElementById('submitButton').textContent = 'Update Food';
        })
        .catch(error => {
            console.error('Error fetching food:', error);
            alert('Failed to fetch food.');
        });
}

function updateFood(id, food) {
    fetch(`/food/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(food),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update food.');
            }
            return response.json();
        })
        .then(() => {
            fetchFoods();
            resetForm();
        })
        .catch(error => {
            console.error('Error updating food:', error);
            alert('Failed to update food.');
        });
}

function deleteFood(id) {
    if (confirm('Are you sure you want to delete this food?')) {
        fetch(`/food/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete food.');
                }
                fetchFoods();
            })
            .catch(error => {
                console.error('Error deleting food:', error);
                alert('Failed to delete food.');
            });
    }
}

function resetForm() {
    document.getElementById('foodId').value = '';
    document.getElementById('name').value = '';
    document.getElementById('price').value = '';
    document.getElementById('size').value = '';
    document.getElementById('submitButton').textContent = 'Add Food';
}