const fetchBtn = document.getElementById('fetchBtn');
const dbStatus = document.getElementById('db-status');
const syncNotice = document.getElementById('sync-notice');
const container = document.getElementById('jobs-container');
const loading = document.getElementById('loading');
const errorMsg = document.getElementById('error-message');

const searchInput = document.getElementById('searchInput');
const countryFilter = document.getElementById('countryFilter');
const typeFilter = document.getElementById('typeFilter');

let allJobs = [];

const countryMetadata = {
    'br': { flag: '🇧🇷', name: 'Brasil' },
    'us': { flag: '🇺🇸', name: 'EUA' },
    'gb': { flag: '🇬🇧', name: 'Reino Unido' },
    'ca': { flag: '🇨🇦', name: 'Canadá' },
    'de': { flag: '🇩🇪', name: 'Alemanha' },
    'fr': { flag: '🇫🇷', name: 'França' }
};

window.addEventListener('DOMContentLoaded', loadInitialData);

async function loadInitialData() {
    try {
        const response = await fetch('/api/jobs');
        const dashboard = await response.json();

        allJobs = dashboard.jobs;

        if (allJobs.length === 0) {
            fetchBtn.textContent = "Buscar Vagas na API";
            dbStatus.textContent = "O banco de dados está vazio.";
        } else {
            fetchBtn.textContent = "🔄 Sincronizar Novas Vagas";
            dbStatus.textContent = `Exibindo ${allJobs.length} vagas do banco local.`;
            renderJobs(allJobs);
        }

        updateSyncNotice(dashboard.lastSync);
    } catch (error) {
        showError("Erro ao carregar dados do servidor.");
    }
}

[searchInput, countryFilter, typeFilter].forEach(el => {
    if (el) el.addEventListener('input', filterJobs);
});

fetchBtn.addEventListener('click', async () => {
    container.innerHTML = '';
    errorMsg.classList.add('hidden');
    loading.classList.remove('hidden');
    fetchBtn.disabled = true;

    try {
        const response = await fetch('/api/jobs/fetch', { method: 'POST' });
        if (!response.ok) throw new Error('Falha na sincronização externa.');

        const dashboard = await response.json();
        allJobs = dashboard.jobs;

        renderJobs(allJobs);
        updateSyncNotice(dashboard.lastSync);

        fetchBtn.textContent = "🔄 Sincronizar Novas Vagas";
        dbStatus.textContent = `Sincronização concluída. ${allJobs.length} vagas totais no banco.`;
    } catch (error) {
        showError(error.message);
    } finally {
        loading.classList.add('hidden');
        fetchBtn.disabled = false;
    }
});

function filterJobs() {
    const searchTerm = searchInput.value.toLowerCase();
    const selectedCountry = countryFilter.value.toLowerCase();
    const selectedType = typeFilter.value;

    const filtered = allJobs.filter(job => {
        const matchesSearch = job.title.toLowerCase().includes(searchTerm) ||
            job.companyName.toLowerCase().includes(searchTerm) ||
            job.technologies.toLowerCase().includes(searchTerm);

        const matchesCountry = selectedCountry === 'all' ||
            (job.countryCode && job.countryCode.toLowerCase() === selectedCountry);

        const matchesType = selectedType === 'all' || job.jobType === selectedType;

        return matchesSearch && matchesCountry && matchesType;
    });

    renderJobs(filtered);
}

function updateSyncNotice(lastSyncServer) {
    syncNotice.classList.remove('hidden');

    if (!lastSyncServer) {
        syncNotice.innerHTML = `ℹ️ Vagas carregadas do banco. Sincronize para buscar atualizações.`;
        return;
    }

    const date = new Date(lastSyncServer);
    const formattedDate = date.toLocaleString('pt-BR', {
        day: '2-digit', month: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });

    syncNotice.innerHTML = `🕒 Última atualização do sistema: ${formattedDate}h`;
}

function getCountryIcon(job) {
    const code = job.countryCode ? job.countryCode.toLowerCase() : '';

    if (countryMetadata[code]) {
        return countryMetadata[code].flag;
    }

    const loc = job.locationName ? job.locationName.toLowerCase() : '';
    if (loc.includes("brazil") || loc.includes("brasil") || loc.includes("estado de")) return "🇧🇷";
    if (loc.includes("usa") || loc.includes("united states") || loc.includes("us")) return "🇺🇸";
    if (loc.includes("united kingdom") || loc.includes("uk") || loc.includes("gb")) return "🇬🇧";

    return "🌎";
}

function renderJobs(jobs) {
    container.innerHTML = '';

    if (jobs.length === 0) {
        container.innerHTML = '<p class="info-msg">Nenhuma vaga atende aos filtros selecionados.</p>';
        return;
    }

    jobs.forEach(job => {
        const isRemote = job.jobType === 'REMOTE';
        const techs = job.technologies ? job.technologies.split(',') : [];

        const countryIcon = getCountryIcon(job);

        const isInternational = job.countryCode && job.countryCode.toLowerCase() !== 'br';

        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <div>
                <div style="display: flex; justify-content: space-between; align-items: center;">
                    <span class="badge ${isRemote ? 'badge-remote' : 'badge-onsite'}">
                        ${isRemote ? '🏠 Remoto' : '🏢 Presencial'}
                    </span>
                    <span style="font-size: 1.2rem;">${countryIcon}</span>
                </div>
                <h3>${job.title}</h3>
                <p class="company">${job.companyName}</p>
                <p class="location">📍 ${job.locationName}</p>
                ${isInternational ? `<p class="warning-text">⚠️ Vaga internacional com possíveis restrições regionais.</p>` : ''}
                <p class="card-description" title="${job.description}">${job.description}</p>
                <div class="tech-tags">
                    ${techs.map(t => `<span class="tech-tag">${t.trim()}</span>`).join('')}
                </div>
                <p class="salary">💰 ${job.salaryRange}</p>
            </div>
            <a href="${job.url}" target="_blank" class="apply-link">Ver Detalhes</a>
        `;
        container.appendChild(card);
    });
}

function showError(msg) {
    errorMsg.textContent = msg;
    errorMsg.classList.remove('hidden');
}