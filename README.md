# PersonaMockServer
A simple mock server that can respond to any request with mocked data (file/custom).  
Each persona defines a profile of consistent always-reproducible data to support regression testing.


I was frustrated that every company I have worked for has poor testing practices and developers/QA test against constantly changing, frequently broken environments where accounts are in short supply and frequently accessed by other individuals.  This web application (runs as a servlet or spark instance) works on the concept of personas (i.e. oliver is poor, richardbranson is rich) that return mock data that is constant.  This allows developers/testers to test scenarios – i.e. will the currency display look good for people with small, large or even negative balances without having to hunt down a valid account every time.
