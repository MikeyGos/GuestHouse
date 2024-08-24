
async function fetchSortedFoods(searchInput = '') {
    let url = '/sortedSnacks';
    if (searchInput) {
        url += `?name=${encodeURIComponent(searchInput)}`;
    }
    try {
        const response = await fetch(url);
        const foods = await response.json();
        localStorage.setItem('food', JSON.stringify(foods));
        displayFood(foods);
    } catch (error) {
        console.error('Error fetching foods:', error);
    }
}


function displayFood(food) {
    const foodTableBody = document.getElementById('foodTableBody');
    foodTableBody.innerHTML = '';

    food.forEach(food => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${food.name}</td>
            <td>${food.price}</td>
            <td>${food.size}</td>
            <td>
                <div class="quantity-controls">
                    <button onclick="updateQuantity('${food.idFood}', '${food.name}', ${food.price}, -1)">-</button>
                    <span id="quantity-${food.idFood}">0</span>
                    <button onclick="updateQuantity('${food.idFood}', '${food.name}', ${food.price}, 1)">+</button>
                </div>
            </td>
        `;
        foodTableBody.appendChild(row);
    });
}


function updateQuantity(idFood, name, price, change) {
    const quantityElement = document.getElementById(`quantity-${idFood}`);
    let currentQuantity = parseInt(quantityElement.textContent);
    currentQuantity += change;
    if (currentQuantity < 0) {
        currentQuantity = 0;
    }
    quantityElement.textContent = currentQuantity;
}

function submitQuantities() {
    const basket = [];
    document.querySelectorAll('span[id^="quantity-"]').forEach(span => {
        const idFood = span.id.replace('quantity-', '');
        const quantity = parseInt(span.textContent);
        if (quantity > 0) {
            const foodRow = span.closest('tr');
            const name = foodRow.querySelector('td:nth-child(1)').textContent;
            const price = parseFloat(foodRow.querySelector('td:nth-child(2)').textContent);
            const totalPrice = (price * quantity).toFixed(2);
            basket.push({idFood, nameBasketProduct: name, qtyBasketProduct: quantity, totalPrice}); // changes name for js.
        }
    });

    localStorage.setItem('basket', JSON.stringify(basket));

    // Opcjonalnie przekieruj użytkownika
    window.location.href = '/basket.html';
}
fetchSortedFoods();