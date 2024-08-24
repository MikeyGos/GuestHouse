document.getElementById('fetchReservationsButton').addEventListener('click', fetchReservations);

function fetchReservations() {
    const date = document.getElementById('reservationDate').value;

    if (!date) {
        alert('Please select a date.');
        return;
    }

    fetch(`/bnb/reservations?date=${date}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch reservations.');
            }
            return response.json();
        })
        .then(reservations => {
            displayReservations(reservations);
        })
        .catch(error => {
            console.error('Error fetching reservations:', error);
            alert('Failed to fetch reservations.');
        });
}

function displayReservations(reservations) {
    const tableBody = document.getElementById('reservationsTableBody');
    tableBody.innerHTML = '';

    if (reservations.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="5">No reservations found for this date.</td></tr>';
        return;
    }

    reservations.forEach(room => {
        const row = document.createElement('tr');
        row.innerHTML = `
                    <td>${room.roomName}</td>
                    <td>${room.date}</td>
                    <td>${room.time}</td>
                    <td>${room.bookingNumber}</td>
                    <td><button onclick="deleteReservation(${room.idParty})">Delete</button></td>
                `;
        tableBody.appendChild(row);
    });
}

function deleteReservation(idParty) {
    if (confirm('Are you sure you want to delete this reservation?')) {
        fetch(`/bnb/room/${idParty}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert('Reservation deleted successfully.');
                    fetchReservations(); // Refresh the table
                } else {
                    throw new Error('Failed to delete reservation.');
                }
            })
            .catch(error => {
                console.error('Error deleting reservation:', error);
                alert('Failed to delete reservation.');
            });
    }
}