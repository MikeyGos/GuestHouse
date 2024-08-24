document.getElementById('bookingNumber').addEventListener('input', fetchUserRooms);

function fetchUserRooms() {
    const bookingNumber = document.getElementById('bookingNumber').value.trim();


    if (!bookingNumber) {
        document.getElementById('roomsTableBody').innerHTML = ''; // Wyczyść tabelę, jeśli brak numeru rezerwacji
        return;
    }

    fetch(`/bnb/room/${bookingNumber}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch rooms. Please check your booking number.');
            }
            return response.json();
        })
        .then(rooms => {
            displayRooms(rooms);
        })
        .catch(error => {
            console.error('Error fetching rooms:', error);
            document.getElementById('roomsTableBody').innerHTML = '<tr><td colspan="4">Failed to fetch rooms. Please try again.</td></tr>';
        });
}

function displayRooms(rooms) {
    const roomsTableBody = document.getElementById('roomsTableBody');
    roomsTableBody.innerHTML = ''; // Clear previous results

    if (rooms.length === 0) {
        roomsTableBody.innerHTML = '<tr><td colspan="4">No rooms found for this booking number.</td></tr>';
        return;
    }

    rooms.forEach(room => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${room.roomName}</td>
            <td>${new Date(room.date).toLocaleDateString()}</td>
            <td>${room.time}</td>
            <td>${room.bookingNumber}</td>
        `;
        roomsTableBody.appendChild(row);
    });
}