document.addEventListener('DOMContentLoaded', function() {
    const basket = JSON.parse(localStorage.getItem('basket')) || [];
    const basketTableBody = document.getElementById('basketTableBody');

    if (basket.length === 0) {
        basketTableBody.innerHTML = '<tr><td colspan="4" style="text-align:center;">No items in the basket.</td></tr>';
        return;
    }

    basket.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.idDrink}</td>
            <td>${item.nameBasketProduct}</td>
            <td>${item.qtyBasketProduct}</td>
            <td>${item.totalPrice}</td>
        `;
        basketTableBody.appendChild(row);
    });
});

// Submit the order using data from localStorage
function submitMyOrder() {
    let data = localStorage.getItem('basket');
    console.log(data);
    fetch('/orderProduct', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    })
        .then(response => response.json())
        .then(responseText => {
            console.log(responseText);
            window.location.href = '/basket.html'; // Redirect to the order confirmation page
        })
        .catch(error => console.error('Error submitting order:', error));
}