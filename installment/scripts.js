document.getElementById('calcForm').addEventListener('submit', function(e) {
    e.preventDefault();
  
    // Retrieve form values
    const annualReturnRateInput = document.getElementById('annualReturnRate').value;
    const annualInflationRateInput = document.getElementById('annualInflationRate').value;
    const totalInstallmentValue = parseFloat(document.getElementById('totalInstallmentValue').value);
    const numberOfInstallments = parseInt(document.getElementById('numberOfInstallments').value, 10);
    const cashValue = parseFloat(document.getElementById('cashValue').value);
  
    // Convert percentage values to decimals
    const annualReturnRate = parseFloat(annualReturnRateInput) / 100;
    const annualInflationRate = parseFloat(annualInflationRateInput) / 100;
  
    // Create the request payload
    const data = {
      annualReturnRate,
      annualInflationRate,
      totalInstallmentValue,
      numberOfInstallments,
      cashValue
    };
  
    // Send the POST request to the API endpoint
    fetch(`${API_URL}/installment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.text();
      })
      .then(result => {
        document.getElementById('result').textContent = result;
      })
      .catch(error => {
        document.getElementById('result').textContent = 'Error: ' + error.message;
      });
  });
  