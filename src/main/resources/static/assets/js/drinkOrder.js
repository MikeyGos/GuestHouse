// Fetch sorted drinks and save to localStorage
async function fetchSortedDrinks(searchInput = '') {
    let url = '/sortedDrinks';
    if (searchInput) {
        url += `?name=${encodeURIComponent(searchInput)}`;
    }
    try {
        const response = await fetch(url);
        const drinks = await response.json();
        localStorage.setItem('drinks', JSON.stringify(drinks)); // Save to localStorage
        displayDrinks(drinks); // Display the drinks
    } catch (error) {
        console.error('Error fetching drinks:', error);
    }
}

// Display drinks in a table
function displayDrinks(drinks) {
    const drinkTableBody = document.getElementById('drinkTableBody');
    drinkTableBody.innerHTML = '';

    drinks.forEach(drink => {
        const row = document.createElement('tr');
        row.setAttribute('data-id', drink.idDrink); // Set idDrink as a data attribute
        row.innerHTML = `
            <td>${drink.name}</td>
            <td>${drink.price}</td>
            <td>${drink.capacity}</td>
            <td>
                <div class="quantity-controls">
                    <button onclick="updateQuantity('${drink.idDrink}', '${drink.name}', ${drink.price}, -1)">-</button>
                    <span id="quantity-${drink.idDrink}">0</span>
                    <button onclick="updateQuantity('${drink.idDrink}', '${drink.name}', ${drink.price}, 1)">+</button>
                </div>
            </td>
        `;
        drinkTableBody.appendChild(row);
    });
}

function updateQuantity(idDrink, drinkName, price, change) {
    const quantityElement = document.getElementById(`quantity-${idDrink}`);
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
        const idDrink = span.id.replace('quantity-', '');
        const quantity = parseInt(span.textContent);
        if (quantity > 0) {
            const drinkRow = span.closest('tr');
            const drinkName = drinkRow.querySelector('td:nth-child(1)').textContent; // First column is name now
            const price = parseFloat(drinkRow.querySelector('td:nth-child(2)').textContent); // Second column is price now
            const totalPrice = (price * quantity).toFixed(2);
            basket.push({ idDrink, nameBasketProduct: drinkName, qtyBasketProduct: quantity, totalPrice });
        }
    });

    localStorage.setItem('basket', JSON.stringify(basket));

    fetch('/basket', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(basket)
    })
        .then(response => response.json())
        .then(responseText => {
            console.log(responseText);
            window.location.href = '/basket.html';
        })
        .catch(error => {
            console.error('Error submitting quantities:', error);
        });
}

fetchSortedDrinks();