// API Base URL - empty means same origin, nginx proxies /api/ to backend
const API_BASE = '';

// DOM Elements
const shortenForm = document.getElementById('shorten-form');
const urlInput = document.getElementById('url-input');
const resultCard = document.getElementById('result');
const shortUrlEl = document.getElementById('short-url');
const originalUrlEl = document.getElementById('original-url');
const copyBtn = document.getElementById('copy-btn');
const refreshBtn = document.getElementById('refresh-btn');
const linksContainer = document.getElementById('links-container');
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

// Create short link
async function createShortLink(url) {
    try {
        const response = await fetch(`${API_BASE}/api/links`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ url })
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка при создании ссылки');
        }
        
        return await response.json();
    } catch (error) {
        console.error('Create link error:', error);
        throw error;
    }
}

// Get all links
async function getAllLinks() {
    try {
        const response = await fetch(`${API_BASE}/api/links`);
        
        if (!response.ok) {
            throw new Error('Ошибка при загрузке ссылок');
        }
        
        const links = await response.json();
        
        // Sort by click count (descending)
        links.sort((a, b) => b.clickCount - a.clickCount);
        
        return links;
    } catch (error) {
        console.error('Get links error:', error);
        throw error;
    }
}

// Delete link
async function deleteLink(code) {
    try {
        const response = await fetch(`${API_BASE}/api/links/${code}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Ошибка при удалении ссылки');
        }
        
        return true;
    } catch (error) {
        console.error('Delete link error:', error);
        throw error;
    }
}

// Render links list
function renderLinks(links) {
    if (links.length === 0) {
        linksContainer.innerHTML = `
            <div class="empty-state">
                <span class="empty-icon">—</span>
                <p>Пока нет ссылок</p>
            </div>
        `;
        return;
    }
    
    linksContainer.innerHTML = links.map(link => `
        <div class="link-card" data-code="${link.code}">
            <div class="link-card-header">
                <a href="${link.shortUrl}" target="_blank" class="link-card-code">
                    ${link.shortUrl}
                </a>
                <div class="link-card-stats">
                    <span>${link.clickCount} кликов</span>
                </div>
            </div>
            <p class="link-card-original">${link.originalUrl}</p>
            <div class="link-card-footer">
                <span class="link-card-date">${formatDate(link.createdAt)}</span>
                <button class="btn-delete" onclick="handleDelete('${link.code}')">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
                    Удалить
                </button>
            </div>
        </div>
    `).join('');
}

// Load and render links
async function loadLinks() {
    try {
        const links = await getAllLinks();
        renderLinks(links);
    } catch (error) {
        showToast('Ошибка загрузки ссылок', 'error');
    }
}

// Handle form submit
shortenForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const url = urlInput.value.trim();
    if (!url) return;
    
    const submitBtn = shortenForm.querySelector('button[type="submit"]');
    const btnText = submitBtn.querySelector('.btn-text');
    const btnLoader = submitBtn.querySelector('.btn-loader');
    
    // Show loading
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');
    submitBtn.disabled = true;
    
    try {
        const link = await createShortLink(url);
        
        // Show result
        shortUrlEl.href = link.shortUrl;
        shortUrlEl.textContent = link.shortUrl;
        originalUrlEl.textContent = link.originalUrl;
        resultCard.classList.remove('hidden');
        
        // Clear input
        urlInput.value = '';
        
        // Reload links
        await loadLinks();
        
        showToast('Ссылка успешно создана!', 'success');
    } catch (error) {
        showToast(error.message || 'Ошибка при создании ссылки', 'error');
    } finally {
        // Hide loading
        btnText.classList.remove('hidden');
        btnLoader.classList.add('hidden');
        submitBtn.disabled = false;
    }
});

// Handle copy button
copyBtn.addEventListener('click', async () => {
    const url = shortUrlEl.href;
    
    try {
        await navigator.clipboard.writeText(url);
        showToast('Ссылка скопирована!', 'success');
    } catch (error) {
        showToast('Не удалось скопировать', 'error');
    }
});

// Handle refresh button
refreshBtn.addEventListener('click', () => {
    loadLinks();
});

// Handle delete
window.handleDelete = async (code) => {
    if (!confirm('Удалить эту ссылку?')) return;
    
    try {
        await deleteLink(code);
        await loadLinks();
        showToast('Ссылка удалена', 'success');
    } catch (error) {
        showToast('Ошибка при удалении', 'error');
    }
};

// Initial load
document.addEventListener('DOMContentLoaded', () => {
    loadLinks();
    
    // Setup Spy Link dynamically
    const spyLink = document.getElementById('spy-link');
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
        // Localhost mode: Port 81
        spyLink.href = 'http://localhost:81';
    } else {
        // Serveo/Prod mode: cors-balls subdomain
        spyLink.href = 'https://cors-balls.serveousercontent.com';
    }
});
