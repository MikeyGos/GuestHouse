document.addEventListener('DOMContentLoaded', function () {
    fetch('/bnb/login')
        .then(response => response.text())
        .then(bookingNumber => {
            if (!bookingNumber) {
                alert('Booking number not found. Please log in.');
                window.location.href = '/login.html';
            } else {
                var calendarEl = document.getElementById('calendar');
                var today = new Date().toISOString().split('T')[0];
                var calendar = new FullCalendar.Calendar(calendarEl, {
                    initialView: 'dayGridMonth',
                    headerToolbar: {
                        left: 'prev,next today',
                        center: 'title',
                        right: ''
                    },
                    height: 'auto',
                    contentHeight: 'auto',
                    fixedWeekCount: false,
                    selectable: true,
                    validRange: {
                        start: today
                    },
                    selectAllow: function (selectInfo) {
                        return selectInfo.start >= new Date();
                    },
                    dateClick: function (info) {
                        if (info.date < new Date().setHours(0, 0)) {
                            alert('You cannot book a date in the past.');
                            return;
                        }

                        var date = info.dateStr;
                        fetch(`/bnb/reservations?date=${date}`)
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.json();
                            })
                            .then(reservations => {
                                var roomName = document.getElementById('roomName').value;
                                var availableTimes;

                                if (roomName === 'Breakfast Room') {
                                    availableTimes = ['08:00', '08:30', '09:00', '09:30', '10:00'];
                                } else {
                                    availableTimes = ['10:00', '12:00', '14:00', '16:00', '18:00'];
                                }

                                var reservedTimes = reservations
                                    .filter(r => r.roomName === 'Breakfast Room' || r.roomName === 'Party Room')
                                    .map(r => r.time.substring(0, 5));

                                var options = availableTimes.filter(time => !reservedTimes.includes(time))
                                    .map(time => `<option value="${time}">${time}</option>`)
                                    .join('');

                                var timeSelect = document.getElementById('time');
                                timeSelect.innerHTML = options;

                                $('#bookingModal').modal('show');

                                var bookingForm = document.getElementById('bookingForm');
                                bookingForm.onsubmit = function (event) {
                                    event.preventDefault();

                                    var roomName = document.getElementById('roomName').value;
                                    var time = document.getElementById('time').value;

                                    var room = {
                                        bookingNumber: bookingNumber,
                                        date: date,
                                        time: time,
                                        roomName: roomName
                                    };

                                    fetch('/bnb/room', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/json'
                                        },
                                        body: JSON.stringify(room)
                                    }).then(response => {
                                        if (response.status === 409) {
                                            alert('You already have a reservation for this room on this date.');
                                            return null;
                                        }
                                        if (!response.ok) {
                                            throw new Error('Failed to create booking');
                                        }
                                        return response.text();
                                    }).then(message => {
                                        if (message !== null) {
                                            alert(`Your room order has been confirmed on ${date} at ${time} in the ${roomName}.`);
                                            calendar.refetchEvents();
                                            $('#bookingModal').modal('hide');
                                        }
                                    }).catch(error => {
                                        console.error('Error:', error);
                                        alert('Failed to create booking.');
                                    });
                                };
                            }).catch(error => {
                            console.error('Error:', error);
                            alert('Failed to fetch reservations.');
                        });
                    }
                });
                calendar.render();

                var roomNameSelect = document.getElementById('roomName');
                roomNameSelect.addEventListener('change', function () {
                    var roomName = roomNameSelect.value;
                    var availableTimes;

                    if (roomName === 'Breakfast Room') {
                        availableTimes = ['08:00', '08:30', '09:00', '09:30', '10:00'];
                    } else {
                        availableTimes = ['10:00', '12:00', '14:00', '16:00', '18:00'];
                    }

                    var timeSelect = document.getElementById('time');
                    timeSelect.innerHTML = availableTimes.map(time => `<option value="${time}">${time}</option>`).join('');
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to fetch booking number.');
        });
});