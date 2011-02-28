(Include   ship   "ship.obj")      ## this calls the polygon in that file "ship"
(Include   star    "star.obj")				

(G trailer
	(I inst1 ship (S 5 5) (color 1 0 0) (T 0 {0.5*cos(t*0.1)}))
)

(G fleet_A
	(I inst1 ship (S 10 10) (T 5 0) (color 1 0 0))		
	(I inst2 trailer (T 0  3))
	(I inst3 trailer (T 0 -3))
	(I inst4 trailer)
)

(G starspin
	(I inst1 star (R {t*20}) (S 3 3))
)

(G spinboarder
	(I i_1 starspin (T {20*sin(t*6*dgr)} 0) (T 0 -19))
	(I i_2 starspin (T {-20*sin(t*6*dgr)} 0) (T 0  19))
	(I i_3 starspin (T 0 {20*sin(t*6*dgr)}) (T 19 0))
	(I i_4 starspin (T 0 {-20*sin(t*6*dgr)}) (T -19 0))
)

(G myScene
	(I i_1 fleet_A (T {cos(t*0.1)} 0))
	(I i_2 spinboarder (S 0.8 0.8) (color {sin(t*0.1)} {cos(t*0.1)} {sin(t*0.1) * 2}))
	(I i_3 spinboarder (S 0.2 0.2) (T -6  3))
	(I i_4 spinboarder (S 0.2 0.2) (T -6 -3))
	(I i_5 spinboarder (S 0.2 0.2) (T -6 -1))
	(I i_6 spinboarder (S 0.2 0.2) (T -6  1))
)

(Render myScene)

