async function fetchSortedDrinks(searchInput = '') {
    let url = '/sortedDrinks';
    if (searchInput) {
        url += `?name=${encodeURIComponent(searchInput)}`;
    }
    try {
        const response = await fetch(url);
        const drinks = await response.json();
        localStorage.setItem('drinks', JSON.stringify(drinks));
        displayDrinks(drinks);
    } catch (error) {
        console.error('Error fetching drinks:', error);
    }
}

function displayDrinks(drinks) {
    const drinkTableBody = document.getElementById('drinkTableBody');
    drinkTableBody.innerHTML = '';

    drinks.forEach(drink => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${drink.name}</td>
            <td>${drink.capacity + ' ml'}</td>
            <td>${drink.price + ' €'}</td>
        `;
        drinkTableBody.appendChild(row);
    });
}

fetchSortedDrinks();