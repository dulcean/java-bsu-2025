// API Base URL - dynamic based on current origin
let API_BASE;

if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    // Localhost: request to port 80 (Main) from port 81 (Spy)
    API_BASE = 'http://localhost';
} else {
    // Serveo: Dynamic, replace 'cors-balls' with 'balls' to find main API
    const mainHost = window.location.hostname.replace('cors-balls', 'balls');
    API_BASE = `${window.location.protocol}//${mainHost}`;
}

// DOM Elements
const fetchLinksBtn = document.getElementById('fetch-links-btn');
const createSpyLinkBtn = document.getElementById('create-spy-link-btn');
const corsStatus = document.getElementById('cors-status');
const statusIndicator = corsStatus.querySelector('.status-indicator');
const statusText = corsStatus.querySelector('.status-text');
const responseContainer = document.getElementById('response-container');
const responseData = document.getElementById('response-data');
const spyLinksContainer = document.getElementById('spy-links-container');
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toast-message');

// Show toast notification
function showToast(message, type = 'info') {
    toastMessage.textContent = message;
    toast.className = `toast ${type}`;
    toast.classList.add('show');
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Update CORS status indicator
function updateStatus(status, message) {
    statusIndicator.className = 'status-indicator ' + status;
    statusText.textContent = message;
}

// Format date
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Render spy links
function renderSpyLinks(links) {
    if (links.length === 0) {
        spyLinksContainer.innerHTML = `
            <div class="empty-state">
                <span class="empty-icon">—</span>
                <p>Нет перехваченных ссылок</p>
            </div>
        `;
        return;
    }
    
    spyLinksContainer.innerHTML = links.map(link => `
        <div class="link-card">
            <div class="link-card-header">
                <span class="link-card-code">${link.code}</span>
                <div class="link-card-stats">
                    <span>${link.clickCount} кликов</span>
                </div>
            </div>
            <p class="link-card-original">
                <strong>Original:</strong> ${link.originalUrl}
            </p>
            <p class="link-card-original">
                <strong>Short:</strong> <a href="${link.shortUrl}" target="_blank">${link.shortUrl}</a>
            </p>
            <div class="link-card-footer">
                <span class="link-card-date">${formatDate(link.createdAt)}</span>
            </div>
        </div>
    `).join('');
}

// Fetch all links (CORS request - will be BLOCKED!)
async function fetchAllLinks() {
    updateStatus('loading', 'Выполняется CORS-запрос к ' + API_BASE + '...');
    
    try {
        const startTime = performance.now();
        
        const response = await fetch(`${API_BASE}/api/links`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        const endTime = performance.now();
        const duration = Math.round(endTime - startTime);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const links = await response.json();
        
        // Sort by click count (descending)
        links.sort((a, b) => b.clickCount - a.clickCount);
        
        // Success! (this shouldn't happen if CORS blocks it)
        updateStatus('success', `CORS разрешён! Время: ${duration}ms | Получено: ${links.length} ссылок`);
        
        // Show response
        responseContainer.classList.remove('hidden');
        responseData.style.color = '#10b981';
        responseData.textContent = JSON.stringify(links, null, 2);
        
        // Render spy links
        renderSpyLinks(links);
        
        showToast('CORS-запрос успешен! Данные получены.', 'success');
        
    } catch (error) {
        console.error('CORS Error:', error);
        
        updateStatus('error', `CORS ЗАБЛОКИРОВАН!`);
        
        responseContainer.classList.remove('hidden');
        responseData.style.color = '#ef4444';
        responseData.textContent = `Ошибка: ${error.message}`;
        
        showToast('CORS-запрос заблокирован браузером!', 'error');
    }
}

// Create spy link (CORS POST request - will be BLOCKED!)
async function createSpyLink() {
    updateStatus('loading', 'Попытка создать ссылку через CORS...');
    
    const spyUrl = 'https://cors-blocked-example.com/spy';
    
    try {
        const response = await fetch(`${API_BASE}/api/links`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ url: spyUrl })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const link = await response.json();
        
        updateStatus('success', `Ссылка создана: ${link.code}`);
        
        responseContainer.classList.remove('hidden');
        responseData.textContent = JSON.stringify(link, null, 2);
        responseData.style.color = '#10b981';
        
        showToast('Ссылка создана!', 'success');
        
    } catch (error) {
        console.error('Create spy link error:', error);
        
        updateStatus('error', `POST запрос заблокирован CORS!`);
        
        responseContainer.classList.remove('hidden');
        responseData.style.color = '#ef4444';
        responseData.textContent = `POST запрос заблокирован!

Ошибка: ${error.message}

CORS блокирует не только GET, но и POST/PUT/DELETE запросы
с других доменов. Это защищает от CSRF атак.`;
        
        showToast('POST заблокирован CORS!', 'error');
    }
}

// Event listeners
fetchLinksBtn.addEventListener('click', fetchAllLinks);
createSpyLinkBtn.addEventListener('click', createSpyLink);

// Check CORS on page load
document.addEventListener('DOMContentLoaded', () => {
    updateStatus('', `Нажмите кнопку для теста CORS-запроса к ${API_BASE}`);
    
    // Setup Back Link dynamically
    const backLink = document.getElementById('back-link');
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
        backLink.href = 'http://localhost';
    } else {
        // Dynamic: go back to main domain
        const mainHost = window.location.hostname.replace('cors-balls', 'balls');
        backLink.href = `${window.location.protocol}//${mainHost}`;
    }
});
