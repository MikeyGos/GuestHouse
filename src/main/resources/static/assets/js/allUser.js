document.addEventListener('DOMContentLoaded', function() {
    fetch('/user')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(users => {
            displayUsers(users);
        })
        .catch(error => {
            console.error('Error fetching users:', error);
            alert('Failed to fetch users.');
        });
});

function displayUsers(users) {
    const userTableBody = document.getElementById('userTableBody');
    userTableBody.innerHTML = ''; // Clear existing content

    users.forEach(user => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${user.bookingNumber}</td>
            <td>${user.name}</td>
            <td>${user.lastName}</td>
            <td>${user.phoneNumber}</td>
        `;

        userTableBody.appendChild(row);
    });
}