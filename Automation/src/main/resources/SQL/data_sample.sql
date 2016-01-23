select *
from test t (nolock)
where t.ID = 1





select top 1 ID
from something s
where s.age > 18

-- random order
SELECT First, Last, NEWID()
FROM AddressBook (nolock)
WHERE PhoneNumber = ?

-- random order
SELECT First, Last
FROM AddressBook (nolock)
WHERE PhoneNumber = ?
order by NEWID()
