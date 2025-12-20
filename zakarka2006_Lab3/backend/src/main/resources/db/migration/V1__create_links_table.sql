-- Создание таблицы links
CREATE TABLE IF NOT EXISTS links (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(6) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    click_count BIGINT NOT NULL DEFAULT 0
);

-- Индекс для быстрого поиска по коду
CREATE INDEX IF NOT EXISTS idx_links_code ON links(code);
