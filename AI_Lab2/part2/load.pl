%%%%%%%%%%%%%
%
% this is a entry point for staring the procedure
% @time 2016/12/2
% @author duocai
%%%%%%%%%%%%%%%%

%load datasets
:- ['data/load_data.pl'].
%load function rules
:- ['function/load_function.pl'].


hp :- 
	write('1. hp: 打开帮助信息。'),nl,
	write('2. guess: 开始猜你所想的小游戏，你在心中拟定一部电影，然后程序通过一系列问题来猜出这部电影。'),nl,
	write('3. guide: 打开荐你所想的功能，不要纠结不会选电影，该看什么电影了，打开它就会引导你，并给出推荐列表。'),nl,
	write('4. hot: 显示当前热门电影，默认为3部'),nl,
	write('5. hot(Num): 显示当前热门电影，Num 指定要显示的电影数目'),nl,
	write('6. show(Name): 查询电影名称为Name的电影信息，Name必须是电影名称'),nl,
	write('7. search(Key): 显示与Key匹配的电影列表，Key可为导演，主演，电影名，类型，评分等信息'),nl.