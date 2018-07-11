

Select * From GameSession s
Inner Join GameConfig c
      On s.gameConfigId == c.id
Inner Join GameKey k
      On c.keyId = k.id
Where k.name == "NBack"
