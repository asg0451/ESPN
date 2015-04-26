DECLARE @Counter INT
DECLARE @Teamid INT
DECLARE	@Rolenum INT
DECLARE @kdr FLOAT



SET @Counter = 1
SET @Teamid = 1
SET @Rolenum = 1
SET @kdr = RAND()+RAND()-- generate random @kdr


WHILE @Counter<=250 -- total 250 players, 50 teams, 5 players per team

	BEGIN
		INSERT INTO Player
		Values ('Player'+CAST(@Counter as varchar(3)), @Teamid, 'Role'+ CAST(@Rolenum as varchar(1)),'Player'+CAST(@Counter as varchar(3))+'@email.com', @kdr )

		SET @Counter = @Counter+1
		SET @kdr = RAND()+RAND()-- regenerate random @kdr

		IF @Rolenum<5 -- 5 roles per team, when 5 roles are used, rolenum reset and teamid increament by one.
			SET @Rolenum = @Rolenum+1
		ELSE
		BEGIN
			SET @Teamid = @Teamid+1
			SET @Rolenum = 1
		END

	END
