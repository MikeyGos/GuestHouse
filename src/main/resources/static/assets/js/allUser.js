document.addEventListener('DOMContentLoaded', function () {
    fetchUsers(); // Pobieranie użytkowników po załadowaniu strony

    // Obsługa dodawania użytkowników
    const addUserForm = document.getElementById('addUserForm');
    addUserForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const newUser = {
            bookingNumber: document.getElementById('bookingNumber').value,
            name: document.getElementById('name').value,
            lastName: document.getElementById('lastName').value,
            phoneNumber: document.getElementById('phoneNumber').value
        };

        fetch('/user', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newUser)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(user => {
                alert('User added successfully!');
                fetchUsers();
            })
            .catch(error => {
                console.error('Error adding user:', error);
                alert('Failed to add user.');
            });
    });
});

// Funkcja pobierająca użytkowników
function fetchUsers() {
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
        });
}


function displayUsers(users) {
    const userTableBody = document.getElementById('userTableBody');
    userTableBody.innerHTML = '';

    users
        .filter(user => user.bookingNumber !== 'Admin')
        .forEach(user => {
            const row = document.createElement('tr');

            row.innerHTML = `
                <td>${user.bookingNumber}</td>
                <td>${user.name}</td>
                <td>${user.lastName}</td>
                <td>${user.phoneNumber}</td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick="populateEditForm('${user.idUser}', '${user.bookingNumber}', '${user.name}', '${user.lastName}', '${user.phoneNumber}')">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteUser('${user.idUser}')">Delete</button>
                </td>
            `;

            userTableBody.appendChild(row);
        });
}

function populateEditForm(idUser, bookingNumber, name, lastName, phoneNumber) {
    document.getElementById('editBookingNumber').value = bookingNumber;
    document.getElementById('editName').value = name;
    document.getElementById('editLastName').value = lastName;
    document.getElementById('editPhoneNumber').value = phoneNumber;

    document.getElementById('editUserForm').style.display = 'block';

    const editUserForm = document.getElementById('editUserForm');
    editUserForm.onsubmit = function (event) {
        event.preventDefault();
        updateUser(idUser);
    };
}

function updateUser(idUser) {
    const updatedUser = {
        bookingNumber: document.getElementById('editBookingNumber').value,
        name: document.getElementById('editName').value,
        lastName: document.getElementById('editLastName').value,
        phoneNumber: document.getElementById('editPhoneNumber').value
    };

    fetch(`/user/${idUser}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(user => {
            alert('User updated successfully!');
            fetchUsers();
        })
        .catch(error => {
            console.error('Error updating user:', error);
            alert('Failed to update user.');
        });
}

function deleteUser(idUser) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    fetch(`/user/${idUser}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            alert('User deleted successfully!');
            fetchUsers();
        })
        .catch(error => {
            console.error('Error deleting user:', error);
            alert('Failed to delete user.');
        });
}