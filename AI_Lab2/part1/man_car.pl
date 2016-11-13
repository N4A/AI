%George is a mechanic. His co-workers, Jimmy and Tito, and their friend Doc often hang out together
%and talk about their cars. In no particular order, one of the men owns a Ford, one owns a Chevrolet,
%one owns a Dodge, and one owns a Toyota. They also talk about their gas mileage, and which of
%their cars is most fuel-efficient. One of the cars does really well and gets 30 miles per gallon of
%gasoline. Another of the cars gets 25 miles to the gallon. Another car gets 20 miles per gallon. And
%the last car gets only 15 miles per gallon。
%
%Also，there is many message in this paragraph, and there is commen templete of the cars.
%So, I use the same method as einstein_puzzle's.
%templete: car(Owner,Brand,mpg)/3

cars(C):- 
	%init default message.
	C = [car(george,_,GM),car(doc,DB,DM),car(tito,_,TM),car(jimmy,_,JM)],!,
	
	%init other default message randomly.
	member(car(_,ford,_),C),
	member(car(_,chevrolet,_),C),
	member(car(_,dodge,_),C),
	member(car(_,toyota,_),C),
	member(car(_,_,30),C),
	member(car(_,_,25),C),
	member(car(_,_,20),C),
	member(car(_,_,10),C),
	
	%distinguish the lies.
	%the two men whose cars have the lowest gas mileage (20 and 15 mpg) are a
	%little embarrassed and will always lie when they talk about gas mileage,
	%whether they are talking about their own car or their friends' cars. 
	%So,we should add codes that distinguish lies when use following rules.

	%Tito said: Doc gets 20 miles per gallon of gas.
	% George's gas mileage is better than Jimmy's
	((TM > 20,DM = 20,GM > JM);%truth
	not(TM > 20;DM = 20;GM > JM)),%lies
	
	%Jimmy said: Doc doesn't drive a Toyota. 
	%Tito's gas mileage is higher than the guy who drives the Dodge
	member(car(_,dodge,XM),C),
	DB \= toyota,% this is the same, whether jimmy lies or not.
	((JM > 20,TM > XM);%truth
	not(JM > 20;TM < XM)),%lies
	
	%George said: The guy who owns the Ford is getting 30 miles per gallon.
	%The guy who gets 20 miles per gallon doesn't own a Chevrolet.
	member(car(_,ford,FM),C),
	member(car(_,chevrolet,CM),C),
	((GM > 20,FM = 30,CM \= 20);%truth
	not(GM > 20;FM = 30;CM \= 20)),%lies
	
	%Doc said: My gas mileage is 20 miles per gallon
	((DM > 20,DM = 20);%truth
	not(DM > 20;DM = 20)).%lies

	
%Question: What kind of car does each man drive and what gas mileage (mpg) does each car get?
%Your answer should be in the following order: George, Doc, Tito, Jimmy.
answer():-cars(C),write(C).
% cars(C).
% answer:
% C=[car(george, chevrolet, 25), car(doc, dodge, 10), 
%	 car(tito, toyota, 20), car(jimmy, ford, 30)] ;