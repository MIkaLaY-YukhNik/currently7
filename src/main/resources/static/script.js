const apiBaseUrl = 'http://localhost:8080/api';
const currencies = ['USD', 'EUR', 'GBP', 'JPY', 'RUB'];

document.addEventListener('DOMContentLoaded', fetchHistory);

document.getElementById('convertForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const amount = document.getElementById('amount').value;
    const fromCurrency = document.getElementById('fromCurrency').value;
    const toCurrency = document.getElementById('toCurrency').value;
    const error = document.getElementById('error');
    const result = document.getElementById('result');

    if (!amount || isNaN(amount) || amount <= 0) {
        error.textContent = 'Введите корректную сумму';
        error.style.display = 'block';
        return;
    }
    error.style.display = 'none';

    try {
        const response = await fetch(`${apiBaseUrl}/currency/convert?from=${fromCurrency}&to=${toCurrency}&amount=${amount}`);
        if (!response.ok) throw new Error('Ошибка конвертации');
        const data = await response.json();
        result.textContent = `Результат: ${amount} ${fromCurrency} = ${data.convertedAmount.toFixed(2)} ${toCurrency}`;
        result.style.display = 'block';

        const historyEntry = {
            fromCurrency,
            toCurrency,
            amount: parseFloat(amount),
            convertedAmount: data.convertedAmount,
            convertedAt: new Date().toISOString(),
            userId: 1,
            currencyRateCodes: [fromCurrency, toCurrency]
        };
        const saveResponse = await fetch(`${apiBaseUrl}/conversion-history`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(historyEntry)
        });
        if (!saveResponse.ok) throw new Error('Ошибка сохранения в историю');
        await fetchHistory(); 
    } catch (err) {
        error.textContent = err.message || 'Ошибка при конвертации валют';
        error.style.display = 'block';
    }
});

async function fetchHistory() {
    try {
        const response = await fetch(`${apiBaseUrl}/conversion-history`);
        if (!response.ok) throw new Error('Ошибка загрузки истории');
        const data = await response.json();
        const tbody = document.getElementById('historyBody');
        tbody.innerHTML = '';
        data.forEach(entry => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${entry.id}</td>
                <td>${entry.amount.toFixed(2)}</td>
                <td>${entry.fromCurrency}</td>
                <td>${entry.toCurrency}</td>
                <td>${entry.convertedAmount.toFixed(2)}</td>
                <td>${new Date(entry.convertedAt).toLocaleString()}</td>
                <td>
                    <button class="action-btn edit-btn" data-id="${entry.id}" data-amount="${entry.amount}" data-from="${entry.fromCurrency}" data-to="${entry.toCurrency}" data-converted="${entry.convertedAmount}" onclick="openEditModal(this)">Редактировать</button>
                    <button class="action-btn delete-btn" onclick="deleteHistory(${entry.id})">Удалить</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (err) {
        document.getElementById('error').textContent = err.message || 'Ошибка при загрузке истории';
        document.getElementById('error').style.display = 'block';
    }
}

async function deleteHistory(id) {
    try {
        const response = await fetch(`${apiBaseUrl}/conversion-history/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) throw new Error('Ошибка удаления');
        await fetchHistory(); 
    } catch (err) {
        document.getElementById('error').textContent = err.message || 'Ошибка при удалении записи';
        document.getElementById('error').style.display = 'block';
    }
}

function openEditModal(button) {
    const id = button.dataset.id;
    const amount = button.dataset.amount;
    const fromCurrency = button.dataset.from;
    const toCurrency = button.dataset.to;
    const convertedAmount = button.dataset.converted;
    document.getElementById('editAmount').value = amount;
    document.getElementById('editFromCurrency').value = fromCurrency;
    document.getElementById('editToCurrency').value = toCurrency;
    document.getElementById('editConvertedAmount').value = convertedAmount;
    document.getElementById('editModal').style.display = 'flex';
    document.getElementById('editForm').onsubmit = async (e) => {
        e.preventDefault();
        const updatedEntry = {
            amount: parseFloat(document.getElementById('editAmount').value),
            fromCurrency: document.getElementById('editFromCurrency').value,
            toCurrency: document.getElementById('editToCurrency').value,
            convertedAmount: parseFloat(document.getElementById('editConvertedAmount').value),
            convertedAt: new Date().toISOString(),
            userId: 1,
            currencyRateCodes: [
                document.getElementById('editFromCurrency').value,
                document.getElementById('editToCurrency').value
            ]
        };
        try {
            const response = await fetch(`${apiBaseUrl}/conversion-history/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedEntry)
            });
            if (!response.ok) throw new Error('Ошибка обновления');
            document.getElementById('editModal').style.display = 'none';
            await fetchHistory(); 
        } catch (err) {
            document.getElementById('error').textContent = err.message || 'Ошибка при обновлении записи';
            document.getElementById('error').style.display = 'block';
        }
    };
}

document.getElementById('cancelEdit').addEventListener('click', () => {
    document.getElementById('editModal').style.display = 'none';
});
