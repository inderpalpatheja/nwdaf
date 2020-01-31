
use Nnwdaf_AnalyticsInfo;
desc eventTable;

select *from eventTable;

select *from load_level_information;

update load_level_information set load_level_info=200 where subscriptionID = 'c048ff3c-ef1b-47ee-81de-23a4d7fcc468';

update load_level_information set load_level_info=300 where subscriptionID='c048ff3c-ef1b-47ee-81de-23a4d7fcc468';

update load_level_information set load_level_info = 1000 where  subscriptionID='c048ff3c-ef1b-47ee-81de-23a4d7fcc468';

