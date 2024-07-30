document.addEventListener('DOMContentLoaded', () => {
    async function fetchProducts() {
        try {
            const response = await fetch('/orderProduct');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            const products = data.products;
            const totalSum = data.totalSum;
            displayProducts(products, totalSum);
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    }

    function displayProducts(products, totalSum) {
        const productList = document.getElementById('basketTableBody');
        productList.innerHTML = '';

        products.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.nameProduct}</td>
                <td>${product.qtyProduct}</td>
                <td>${formatPrice(product.totalPrice)}</td>
                <td><button class="btn btn-primary">Remove</button></td>
            `;
            productList.appendChild(row);
        });


        const summaryRow = document.createElement('tr');
        summaryRow.innerHTML = `
            <td colspan="2"><strong>Total:</strong></td>
            <td><strong>${formatPrice(totalSum)}</strong></td>
            <td></td>
        `;
        productList.appendChild(summaryRow);
    }

    function formatPrice(price) {
        return `${price} €`;
    }

    fetchProducts();
});