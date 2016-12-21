/*
%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% implement the function that guide the 
% users to choose their movie
% @time 2016/12/4
% @author duocai
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
*/

:- module(guides,[
		guide/0
	]).

guide :- 
	findall(
		movie(Name,Director,Starring,Year,Area,Type,Score,Eval),
		get_basic_eval(Name,Director,Starring,Year,Area,Type,Score,Eval),
		All
	),%get all movies
	(%性别属性加分。
		(
			boy,
			type_add_score(All,'动作',[],H1),
			type_add_score(H1,'悬疑',[],Handled)
		);
		(
			type_add_score(All,'爱情',[],H1),
			area_add_score(H1,'韩国',[],Handled)
		)
	),
	get_scene(Scene),%场景属性加成
	scene_handler(Handled,Scene).

get_basic_eval(Name,Director,Starring,Year,Area,Type,Score,Eval) :- 
	movies:movie(Name,Director,Starring,Year,Area,Type,Score,Num,_),
	%calculate the evaluation socre, can be extended more complex in future.
	Eval is (Score*0.3 + Num*0.000002).


%场景处理，获得场景	
get_scene(Scene):-
	write('请选择如下场景之一：'),nl,
	write('1. 和家人一起看.'),nl,
	write('2. 和朋友一起看.'),nl,
	(
		(
			boy,
			write('3. 和女朋友一起看.'),nl
		);
		write('3. 和男朋友一起看.'),nl
	),
	write('4. 自己一个人看.'),nl,
	read(Res),
	(
		(
			int(Res),
			Scene = Res
		);
		get_scene(Scene)
	).

int(1).
int(2).
int(3).
int(4).	
%根据场景给电影评分。
scene_handler(All,1) :-
	type_add_score(All,'家庭',[],Handled1),
	type_add_score(Handled1,'动画',[],Handled),
	type_handler(Handled).
scene_handler(All,2) :-
	type_add_score(All,'剧情',[],Handled),
	type_handler(Handled).
scene_handler(All,3) :-
	type_add_score(All,'爱情',[],Handled1),
	type_add_score(Handled1,'爱情',[],Handled),
	type_handler(Handled).
scene_handler(All,4) :-
	type_add_score(All,'音乐',[],Handled1),
	type_sub_score(Handled1,'爱情',[],Handled),
	type_handler(Handled).


type_handler(All):-
	findall(Type,hypoth_type(Type),Types),
	types_add_score(All,Types,Handled),
	time_handler(Handled).
	
time_handler(All):-
	nostalgia,
	time_add_score(All,2000,[],Handled,0),
	area_handler(Handled).
time_handler(All):-
	not(nostalgia),
	time_add_score(All,2000,[],Handled,1),
	area_handler(Handled).

area_handler(All):-
	america,
	area_add_score(All,'美国',[],Handled),
	show_recomend(Handled).
	
area_handler(All):-
	not(america),
	area_sub_score(All,'美国',[],Handled),
	show_recomend(Handled).

show_recomend(All):-
	write('以下电影可能比较适合您'),nl,
	quick_sort(All,Sorted),!,%no backtrace
	(
		print_movie_brief(Sorted,0,5);
		1=1%succ
	),
	undo.%清除记录

%show brief infos of the movies	
print_movie_brief(All,Index,Num) :- 
	Index < Num,
	nth0(Index,All,Movie),
	movie(Name,_,_,_,_,_,_,Eval) = Movie,
	Next is (Index+1),
	format('第~d部',[Next]),nl,%Index start from 0
	write('综合指标':Eval),nl,
	write('电影':Name),nl,
	info(Name,Plot),
	shows:show_plot(Plot),nl,
	print_movie_brief(All,Next,Num).%recusion to print all
	
%implement the specific quick sort for 
%this templete: movie(Name,Director,Starring,Year,Area,Type,Score,Eval)
quick_sort(List,Sorted):-q_sort(List,[],Sorted).
q_sort([],Acc,Acc).
q_sort([H|T],Acc,Sorted):-
    pivoting(H,T,L1,L2),
    q_sort(L1,Acc,Sorted1),
    q_sort(L2,[H|Sorted1],Sorted).
    
pivoting(_,[],[],[]).
pivoting(H,[X|T],[X|L],G):-
	movie(_,_,_,_,_,_,_,S1) = X,
	movie(_,_,_,_,_,_,_,S2) = H,
	S1 =< S2,
	pivoting(H,T,L,G).
pivoting(H,[X|T],L,[X|G]):-
	movie(_,_,_,_,_,_,_,S1) = X,
	movie(_,_,_,_,_,_,_,S2) = H,
	S1 > S2,
	pivoting(H,T,L,G).

%use types to add score
types_add_score(All,[],All).
types_add_score(All,[T|L],Added):-
	type_add_score(All,T,[],Addone),
	types_add_score(Addone,L,Added).

%use type to add score
type_add_score([],_,All,All).
type_add_score([movie(Name,Director,Starring,Year,Area,Types,Score,Eval)|L],Type,Temp,All) :-
	(
		(
			member(Type,Types),
			Added is Eval + 1,
			append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Added)],New)
		);
		append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Eval)],New)		
	),
	type_add_score(L,Type,New,All).
	
%use type to add score
type_sub_score([],_,All,All).
type_sub_score([movie(Name,Director,Starring,Year,Area,Types,Score,Eval)|L],Type,Temp,All) :-
	(
		(
			member(Type,Types),
			Added is Eval - 1,
			append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Added)],New)
		);
		append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Eval)],New)		
	),
	type_sub_score(L,Type,New,All).

%use time to add score
time_add_score([],_,All,All,_).
time_add_score([movie(Name,Director,Starring,Year,Area,Types,Score,Eval)|L],Time,Temp,All,Sign) :-
	(
		(
			(
				(
					Sign = 0,
					Year < Time
				);
				Year >= Time
			),
			Added is Eval + 2,
			append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Added)],New)
		);
		append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Eval)],New)		
	),
	time_add_score(L,Time,New,All,Sign).

%use area to add score
area_add_score([],_,All,All).
area_add_score([movie(Name,Director,Starring,Year,Area,Types,Score,Eval)|L],Pos,Temp,All) :-
	(
		(
			Area = Pos,
			Added is Eval + 1.5,
			append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Added)],New)
		);
		append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Eval)],New)		
	),
	area_add_score(L,Pos,New,All).
	
%use area to substract score
area_sub_score([],_,All,All).
area_sub_score([movie(Name,Director,Starring,Year,Area,Types,Score,Eval)|L],Pos,Temp,All) :-
	(
		(
			Area = Pos,
			Added is Eval - 1.5,
			append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Added)],New)
		);
		append(Temp,[movie(Name,Director,Starring,Year,Area,Types,Score,Eval)],New)		
	),
	area_sub_score(L,Pos,New,All).

hypoth_type('喜剧') :-
	not(verify('你现在心情好吗?')).

hypoth_type('恐怖') :-
	exciting,
	terror.
	
hypoth_type('惊悚') :-
	exciting,
	terror.

hypoth_type('冒险') :-
	exciting.
	
hypoth_type('科幻') :-
	verify('你是技术控吗？').

hypoth_type('悬疑') :-
	logic.

hypoth_type('推理') :-
	logic.

hypoth_type('剧情') :-
	logic.

boy :- 
	verify('你是男生吗？').
exciting :- 
	verify('你生活中喜欢刺激吗？').
terror :-
	verify('承受的住恐怖惊悚一点的吗？').
logic :- 
	verify('喜欢推理吗？').

nostalgia :-
	verify('是怀旧派吗？').
	
america :-
	verify('是美剧党吗？').
	
%decide the score for each movie
%according to the user's reply
% 你现在心情好吗：喜剧
% 喜欢听音乐吗：音乐
% 你喜欢刺激吗：冒险，恐怖，惊悚
% 是技术控吗：科幻
% 是怀旧派吗？
% 喜欢推理吗？
% 是男生吗
% 有家庭吗
% 是和同学朋友一起看。
% 是和家人一起看吗 ，
% 是和男朋友一起看吗
% 是和女朋友一起看吗
/*
	some basic rules
*/
% ask questions
ask(Question) :-
    write(Question),
    write("y or n:"),
    read(Response),
    ( (Response == y ; Response == yes)
      ->
       assert(yes(Question)) ;
       assert(no(Question)), fail).

:- dynamic yes/1,no/1.

% verify the question
verify(S) :-
   (
   	yes(S) -> true ;
    (no(S) -> fail; ask(S))%no -> stop and back trace
    ).

% undo all yes/no assertions
undo :- retract(yes(_)),fail. 
undo :- retract(no(_)),fail.
undo.
	
