ALTER TABLE exit ADD COLUMN synced INTEGER DEFAULT 0;
ALTER TABLE exit ADD COLUMN deleted INTEGER DEFAULT 0;

ALTER TABLE gear ADD COLUMN synced INTEGER DEFAULT 0;
ALTER TABLE gear ADD COLUMN deleted INTEGER DEFAULT 0;