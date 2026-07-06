create index idx_spot_status_id on spot (status, id);
create index idx_spot_latitude_longitude on spot (latitude, longitude);
create index idx_parking_session_plate_status_entry_time on parking_session (plate, status, entry_time);