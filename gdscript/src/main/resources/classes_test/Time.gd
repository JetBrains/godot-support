extends Object
class_name Time

const MONTH_JANUARY = 1;
const MONTH_FEBRUARY = 2;
const MONTH_MARCH = 3;
const MONTH_APRIL = 4;
const MONTH_MAY = 5;
const MONTH_JUNE = 6;
const MONTH_JULY = 7;
const MONTH_AUGUST = 8;
const MONTH_SEPTEMBER = 9;
const MONTH_OCTOBER = 10;
const MONTH_NOVEMBER = 11;
const MONTH_DECEMBER = 12;
const WEEKDAY_SUNDAY = 0;
const WEEKDAY_MONDAY = 1;
const WEEKDAY_TUESDAY = 2;
const WEEKDAY_WEDNESDAY = 3;
const WEEKDAY_THURSDAY = 4;
const WEEKDAY_FRIDAY = 5;
const WEEKDAY_SATURDAY = 6;


func get_date_dict_from_system(utc: bool) -> Dictionary:
    pass;

func get_date_dict_from_unix_time(unix_time_val: int) -> Dictionary:
    pass;

func get_date_string_from_system(utc: bool) -> String:
    pass;

func get_date_string_from_unix_time(unix_time_val: int) -> String:
    pass;

func get_datetime_dict_from_string(datetime: String, weekday: bool) -> Dictionary:
    pass;

func get_datetime_dict_from_system(utc: bool) -> Dictionary:
    pass;

func get_datetime_dict_from_unix_time(unix_time_val: int) -> Dictionary:
    pass;

func get_datetime_string_from_dict(datetime: Dictionary, use_space: bool) -> String:
    pass;

func get_datetime_string_from_system(utc: bool, use_space: bool) -> String:
    pass;

func get_datetime_string_from_unix_time(unix_time_val: int, use_space: bool) -> String:
    pass;

func get_ticks_msec() -> int:
    pass;

func get_ticks_usec() -> int:
    pass;

func get_time_dict_from_system(utc: bool) -> Dictionary:
    pass;

func get_time_dict_from_unix_time(unix_time_val: int) -> Dictionary:
    pass;

func get_time_string_from_system(utc: bool) -> String:
    pass;

func get_time_string_from_unix_time(unix_time_val: int) -> String:
    pass;

func get_time_zone_from_system() -> Dictionary:
    pass;

func get_unix_time_from_datetime_dict(datetime: Dictionary) -> int:
    pass;

func get_unix_time_from_datetime_string(datetime: String) -> int:
    pass;

func get_unix_time_from_system() -> float:
    pass;

