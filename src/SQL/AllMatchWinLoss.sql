SELECT pm.match_id, p.first_name,p.last_name,pm.wins, CASE WHEN pm.team = m.winning_team THEN'WIN' ELSE'LOSS' END as Outcome, pm.elo_at_time
FROM player p
INNER JOIN player_match pm
        on p.id = pm.player_id
        INNER JOIN match m
        on m.id = pm.match_id
        Order By match_id;