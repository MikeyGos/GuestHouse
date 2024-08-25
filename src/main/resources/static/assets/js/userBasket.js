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

        products.sort((a, b) => {
            if (a.idFood && !b.idFood) return -1;
            if (!a.idFood && b.idFood) return 1;
            return 0;
        });

        products.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.nameProduct}</td>
                <td>${product.qtyProduct}</td>
                <td>${formatPrice(product.totalPrice)}</td>
                <td>
                    <button class="btn btn-primary remove-btn" 
                            data-id-drink="${product.idDrink || ''}" 
                            data-id-food="${product.idFood || ''}">
                        Remove
                    </button>
                </td>
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

        document.querySelectorAll('.remove-btn').forEach(button => {
            button.addEventListener('click', async (event) => {
                const idDrink = event.target.getAttribute('data-id-drink');
                const idFood = event.target.getAttribute('data-id-food');
                await removeProduct(idDrink, idFood);
            });
        });
    }

    async function removeProduct(idDrink, idFood) {
        try {
            const params = new URLSearchParams();
            if (idDrink) params.append('idDrink', idDrink);
            if (idFood) params.append('idFood', idFood);

            const response = await fetch(`/orderProduct?${params.toString()}`, {
                method: 'DELETE',
            });
            if (response.ok) {
                alert('Product removed successfully');
                fetchProducts();
            } else {
                alert('Failed to remove product');
            }
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
            alert('Error removing product');
        }
    }

    function formatPrice(price) {
        return `${price} €`;
    }

    setInterval(fetchProducts, 10000);

    fetchProducts();
});