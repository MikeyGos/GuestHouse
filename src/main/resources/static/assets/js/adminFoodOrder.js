document.addEventListener('DOMContentLoaded', function() {
    const fetchOrdersButton = document.getElementById('fetchOrdersButton');

    fetchOrdersButton.addEventListener('click', fetchUserOrders);
});

function fetchUserOrders() {
    const bookingNumber = document.getElementById('bookingNumber').value.trim();

    if (!bookingNumber) {
        alert('Please enter your booking number.');
        return;
    }

    fetch(`/${bookingNumber}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch orders. Please check your booking number.');
            }
            return response.json();
        })
        .then(orders => {
            displayOrders(orders);
        })
        .catch(error => {
            console.error('Error fetching orders:', error);
            alert('Failed to fetch orders.');
        });
}

function displayOrders(orders) {
    const ordersTableBody = document.getElementById('ordersTableBody');
    ordersTableBody.innerHTML = '';

    if (orders.length === 0) {
        ordersTableBody.innerHTML = '<tr><td colspan="3">No orders found for this booking number.</td></tr>';
        return;
    }

    let totalPriceSum = 0;
    orders.forEach(order => {
        const row = document.createElement('tr');
        const orderPrice = parseFloat(order.totalPrice);

        row.innerHTML = `
            <td>${order.nameProduct}</td>
            <td>${order.qtyProduct}</td>
            <td>${orderPrice.toFixed(2)} euro</td>
        `;

        ordersTableBody.appendChild(row);

        totalPriceSum += orderPrice;
    });

    const totalRow = document.createElement('tr');
    totalRow.innerHTML = `
        <td colspan="2"><strong>Total Price</strong></td>
        <td><strong>${totalPriceSum.toFixed(2)} euro</strong></td>
    `;
    ordersTableBody.appendChild(totalRow);
}