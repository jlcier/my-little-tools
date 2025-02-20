document.getElementById('fuelForm').addEventListener('submit', function(event) {
    event.preventDefault();
  
    // Get the input values
    const kmRemaining = parseFloat(document.getElementById('kmRemaining').value);
    const litersFor100Km = parseFloat(document.getElementById('litersFor100Km').value);
    const capacity = parseFloat(document.getElementById('capacity').value);
    const literCost = parseFloat(document.getElementById('literCost').value);
  
    // Create the request object
    const requestData = {
      kmRemaining: kmRemaining,
      litersFor100Km: litersFor100Km,
      capacity: capacity,
      literCost: literCost
    };
  
    // Show a temporary message
    const resultDiv = document.getElementById('result');
    resultDiv.textContent = 'Calculating...';
  
    // Call the API endpoint
    fetch(`${API_URL}/fuel`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Server error, please try again later.');
      }
      return response.text();
    })
    .then(result => {
      resultDiv.textContent = 'Cost to pay: ' + result;
    })
    .catch(error => {
      resultDiv.textContent = 'Error: ' + error.message;
    });
  });
  