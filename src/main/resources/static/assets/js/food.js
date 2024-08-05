async function fetchSortedFoods(searchInput = '') {
    let url = '/sortedSnacks';
    if (searchInput) {
        url += `?name=${encodeURIComponent(searchInput)}`;
    }
    try {
        const response = await fetch(url);
        const foods = await response.json();
        displayFood(foods);
    } catch (error) {
        console.error('Error fetching foods:', error);
    }
}

function displayFood(foods) {
    const foodTableBody = document.getElementById('foodTableBody');
    foodTableBody.innerHTML = '';

    foods.forEach(food => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${food.name}</td>
            <td>${food.size}</td>
            <td>${food.price+ ' €'}</td>
        `;
        foodTableBody.appendChild(row);
    });
}

fetchSortedFoods();