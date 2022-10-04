#brief Time singleton for working with time.
#desc The Time singleton allows converting time between various formats and also getting time information from the system.
#desc This class conforms with as many of the ISO 8601 standards as possible. All dates follow the Proleptic Gregorian calendar. As such, the day before [code]1582-10-15[/code] is [code]1582-10-14[/code], not [code]1582-10-04[/code]. The year before 1 AD (aka 1 BC) is number [code]0[/code], with the year before that (2 BC) being [code]-1[/code], etc.
#desc Conversion methods assume "the same timezone", and do not handle timezone conversions or DST automatically. Leap seconds are also not handled, they must be done manually if desired. Suffixes such as "Z" are not handled, you need to strip them away manually.
#desc When getting time information from the system, the time can either be in the local timezone or UTC depending on the [code]utc[/code] parameter. However, the [method get_unix_time_from_system] method always returns the time in UTC.
#desc [b]Important:[/b] The [code]_from_system[/code] methods use the system clock that the user can manually set. [b]Never use[/b] this method for precise time calculation since its results are subject to automatic adjustments by the user or the operating system. [b]Always use[/b] [method get_ticks_usec] or [method get_ticks_msec] for precise time calculation instead, since they are guaranteed to be monotonic (i.e. never decrease).
class_name Time

#desc The month of January, represented numerically as [code]01[/code].
const MONTH_JANUARY = 1;

#desc The month of February, represented numerically as [code]02[/code].
const MONTH_FEBRUARY = 2;

#desc The month of March, represented numerically as [code]03[/code].
const MONTH_MARCH = 3;

#desc The month of April, represented numerically as [code]04[/code].
const MONTH_APRIL = 4;

#desc The month of May, represented numerically as [code]05[/code].
const MONTH_MAY = 5;

#desc The month of June, represented numerically as [code]06[/code].
const MONTH_JUNE = 6;

#desc The month of July, represented numerically as [code]07[/code].
const MONTH_JULY = 7;

#desc The month of August, represented numerically as [code]08[/code].
const MONTH_AUGUST = 8;

#desc The month of September, represented numerically as [code]09[/code].
const MONTH_SEPTEMBER = 9;

#desc The month of October, represented numerically as [code]10[/code].
const MONTH_OCTOBER = 10;

#desc The month of November, represented numerically as [code]11[/code].
const MONTH_NOVEMBER = 11;

#desc The month of December, represented numerically as [code]12[/code].
const MONTH_DECEMBER = 12;

#desc The day of the week Sunday, represented numerically as [code]0[/code].
const WEEKDAY_SUNDAY = 0;

#desc The day of the week Monday, represented numerically as [code]1[/code].
const WEEKDAY_MONDAY = 1;

#desc The day of the week Tuesday, represented numerically as [code]2[/code].
const WEEKDAY_TUESDAY = 2;

#desc The day of the week Wednesday, represented numerically as [code]3[/code].
const WEEKDAY_WEDNESDAY = 3;

#desc The day of the week Thursday, represented numerically as [code]4[/code].
const WEEKDAY_THURSDAY = 4;

#desc The day of the week Friday, represented numerically as [code]5[/code].
const WEEKDAY_FRIDAY = 5;

#desc The day of the week Saturday, represented numerically as [code]6[/code].
const WEEKDAY_SATURDAY = 6;




#desc Returns the current date as a dictionary of keys: [code]year[/code], [code]month[/code], [code]day[/code], and [code]weekday[/code].
#desc The returned values are in the system's local time when [param utc] is [code]false[/code], otherwise they are in UTC.
func get_date_dict_from_system() -> Dictionary:
	pass;

#desc Converts the given Unix timestamp to a dictionary of keys: [code]year[/code], [code]month[/code], [code]day[/code], and [code]weekday[/code].
func get_date_dict_from_unix_time() -> Dictionary:
	pass;

#desc Returns the current date as an ISO 8601 date string (YYYY-MM-DD).
#desc The returned values are in the system's local time when [param utc] is [code]false[/code], otherwise they are in UTC.
func get_date_string_from_system() -> String:
	pass;

#desc Converts the given Unix timestamp to an ISO 8601 date string (YYYY-MM-DD).
func get_date_string_from_unix_time() -> String:
	pass;

#desc Converts the given ISO 8601 date and time string (YYYY-MM-DDTHH:MM:SS) to a dictionary of keys: [code]year[/code], [code]month[/code], [code]day[/code], [code]weekday[/code], [code]hour[/code], [code]minute[/code], and [code]second[/code].
#desc If [param weekday] is [code]false[/code], then the [code]weekday[/code] entry is excluded (the calculation is relatively expensive).
#desc [b]Note:[/b] Any decimal fraction in the time string will be ignored silently.
func get_datetime_dict_from_datetime_string(datetime: String, weekday: bool) -> Dictionary:
	pass;

#desc Returns the current date as a dictionary of keys: [code]year[/code], [code]month[/code], [code]day[/code], [code]weekday[/code], [code]hour[/code], [code]minute[/code], [code]second[/code], and [code]dst[/code] (Daylight Savings Time).
func get_datetime_dict_from_system() -> Dictionary:
	pass;

#desc Converts the given Unix timestamp to a dictionary of keys: [code]year[/code], [code]month[/code], [code]day[/code], and [code]weekday[/code].
#desc The returned Dictionary's values will be the same as the [method get_datetime_dict_from_system] if the Unix timestamp is the current time, with the exception of Daylight Savings Time as it cannot be determined from the epoch.
func get_datetime_dict_from_unix_time() -> Dictionary:
	pass;

#desc Converts the given dictionary of keys to an ISO 8601 date and time string (YYYY-MM-DDTHH:MM:SS).
#desc The given dictionary can be populated with the following keys: [code]year[/code], [code]month[/code], [code]day[/code], [code]hour[/code], [code]minute[/code], and [code]second[/code]. Any other entries (including [code]dst[/code]) are ignored.
#desc If the dictionary is empty, [code]0[/code] is returned. If some keys are omitted, they default to the equivalent values for the Unix epoch timestamp 0 (1970-01-01 at 00:00:00).
#desc If [param use_space] is [code]true[/code], the date and time bits are separated by an empty space character instead of the letter T.
func get_datetime_string_from_datetime_dict(datetime: Dictionary, use_space: bool) -> String:
	pass;

#desc Returns the current date and time as an ISO 8601 date and time string (YYYY-MM-DDTHH:MM:SS).
#desc The returned values are in the system's local time when [param utc] is [code]false[/code], otherwise they are in UTC.
#desc If [param use_space] is [code]true[/code], the date and time bits are separated by an empty space character instead of the letter T.
func get_datetime_string_from_system(utc: bool, use_space: bool) -> String:
	pass;

#desc Converts the given Unix timestamp to an ISO 8601 date and time string (YYYY-MM-DDTHH:MM:SS).
#desc If [param use_space] is [code]true[/code], the date and time bits are separated by an empty space character instead of the letter T.
func get_datetime_string_from_unix_time(unix_time_val: int, use_space: bool) -> String:
	pass;

#desc Converts the given timezone offset in minutes to a timezone offset string. For example, -480 returns "-08:00", 345 returns "+05:45", and 0 returns "+00:00".
func get_offset_string_from_offset_minutes() -> String:
	pass;

#desc Returns the amount of time passed in milliseconds since the engine started.
#desc Will always be positive or 0 and uses a 64-bit value (it will wrap after roughly 500 million years).
func get_ticks_msec() -> int:
	pass;

#desc Returns the amount of time passed in microseconds since the engine started.
#desc Will always be positive or 0 and uses a 64-bit value (it will wrap after roughly half a million years).
func get_ticks_usec() -> int:
	pass;

#desc Returns the current time as a dictionary of keys: [code]hour[/code], [code]minute[/code], and [code]second[/code].
#desc The returned values are in the system's local time when [param utc] is [code]false[/code], otherwise they are in UTC.
func get_time_dict_from_system() -> Dictionary:
	pass;

#desc Converts the given time to a dictionary of keys: [code]hour[/code], [code]minute[/code], and [code]second[/code].
func get_time_dict_from_unix_time() -> Dictionary:
	pass;

#desc Returns the current time as an ISO 8601 time string (HH:MM:SS).
#desc The returned values are in the system's local time when [param utc] is [code]false[/code], otherwise they are in UTC.
func get_time_string_from_system() -> String:
	pass;

#desc Converts the given Unix timestamp to an ISO 8601 time string (HH:MM:SS).
func get_time_string_from_unix_time() -> String:
	pass;

#desc Returns the current time zone as a dictionary of keys: [code]bias[/code] and [code]name[/code]. The [code]bias[/code] value is the offset from UTC in minutes, since not all time zones are multiples of an hour from UTC.
func get_time_zone_from_system() -> Dictionary:
	pass;

#desc Converts a dictionary of time values to a Unix timestamp.
#desc The given dictionary can be populated with the following keys: [code]year[/code], [code]month[/code], [code]day[/code], [code]hour[/code], [code]minute[/code], and [code]second[/code]. Any other entries (including [code]dst[/code]) are ignored.
#desc If the dictionary is empty, [code]0[/code] is returned. If some keys are omitted, they default to the equivalent values for the Unix epoch timestamp 0 (1970-01-01 at 00:00:00).
#desc You can pass the output from [method get_datetime_dict_from_unix_time] directly into this function and get the same as what was put in.
#desc [b]Note:[/b] Unix timestamps are often in UTC. This method does not do any timezone conversion, so the timestamp will be in the same timezone as the given datetime dictionary.
func get_unix_time_from_datetime_dict() -> int:
	pass;

#desc Converts the given ISO 8601 date and/or time string to a Unix timestamp. The string can contain a date only, a time only, or both.
#desc [b]Note:[/b] Unix timestamps are often in UTC. This method does not do any timezone conversion, so the timestamp will be in the same timezone as the given datetime string.
#desc [b]Note:[/b] Any decimal fraction in the time string will be ignored silently.
func get_unix_time_from_datetime_string() -> int:
	pass;

#desc Returns the current Unix timestamp in seconds based on the system time in UTC. This method is implemented by the operating system and always returns the time in UTC.
#desc [b]Note:[/b] Unlike other methods that use integer timestamps, this method returns the timestamp as a [float] for sub-second precision.
func get_unix_time_from_system() -> float:
	pass;


