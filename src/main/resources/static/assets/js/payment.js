document.addEventListener('DOMContentLoaded', () => {
    const payOnlineCheckbox = document.getElementById('payOnline');
    const payOnSiteCheckbox = document.getElementById('payOnSite');
    const paymentForm = document.getElementById('paymentForm');

    payOnlineCheckbox.addEventListener('change', () => {
        if (payOnlineCheckbox.checked) {
            payOnSiteCheckbox.checked = false;
        }
    });

    payOnSiteCheckbox.addEventListener('change', () => {
        if (payOnSiteCheckbox.checked) {
            payOnlineCheckbox.checked = false;
        }
    });

    paymentForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const paymentMethod = payOnlineCheckbox.checked ? 'payOnline' : 'payOnSite';
        const data = {
            payOnline: paymentMethod === 'payOnline',
            payOnSite: paymentMethod === 'payOnSite'
        };

        try {
            const response = await fetch('/paymentMethod', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                alert('Payment method updated successfully');
                window.location.href = 'index.html';  // Redirect to index.html
            } else {
                alert('Failed to update payment method');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error updating payment method');
        }
    });
});