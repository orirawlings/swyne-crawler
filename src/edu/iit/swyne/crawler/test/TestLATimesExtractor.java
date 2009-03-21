package edu.iit.swyne.crawler.test;

import java.net.URL;
import java.util.Date;

import edu.iit.swyne.crawler.NewsDocument;
import edu.iit.swyne.crawler.extractor.LATimesExtractor;
import junit.framework.TestCase;

public class TestLATimesExtractor extends TestCase {
	private LATimesExtractor extractor;
	private String source = "http://feedproxy.google.com/~r/latimes/news/~3/ww9TE5mixx8/la-me-outthere6-2009feb06,0,1469200.story";
	private String title = "Small service makes big difference on L.A.'s skid row";
	private String collection = "LA Times";
	private String article;
	private Date publishedDate = new Date(1233907200000L);
	
	protected void setUp() throws Exception {
		extractor = new LATimesExtractor(new URL(source), title, publishedDate, collection);
	}
	
	public void testSimpleLATimesExtraction() throws Exception {
		article = "";
		article += "The trappings of the lives of Krystle Marage and her three daughters are not unusual. There are hairbrushes and loofah sponges; Game Boys and skateboards; school books and Bibles; clothes, clothes and more clothes. These days, they have to fit it all inside four trash cans, which sit alongside 500 others in a dank warehouse, around the corner from a frozen fish distributor and a cheap hotel.\n";
		article += "Marage, 46, grew up on a pig-and-chicken farm in Belize. The girls' father checked out long ago, she said. She's never had money, not in Belize, not in New York, where she  immigrated in 1993, and not in L.A., where she arrived last year after friends convinced her there were jobs to be had. She's always made it, one way or another.\n";
		article += "Two weeks ago, luck ran out. Unable to find work and living on $359 a month in county general-relief assistance, Marage couldn't carry the rent on the one-bedroom space where they'd been staying in the South Park district, not far from Staples Center. She and her daughters landed on skid row.\n";
		article += "Marage, a devout Christian, is sure the devil is after her. Authorities offer a more temporal explanation. The economy, they say, has soured to the point that skid row's sad parade of junkies, drunks and the mentally ill is not only swelling, but is increasingly peppered with new faces.\n";
		article += "Many are new to homelessness. Some are educated professionals -- a few still carry briefcases -- and one, a few weeks back, was so confident that he was but a temporary visitor that he arrived clutching a pair of unused golf cleats.  Long after it became city policy that skid row is no place for children, a jarring number of the newcomers are mothers and their children.\n";
		article += "So, at the warehouse run by the nonprofit Central City East Assn., where the homeless have long stored their belongings in trash cans that are gently referred to as \"bins,\" operators are contending with a clientele they've never had before. The shift, they said, is subtle but real, and they are scrambling to respond.\n";
		article += "Last weekend, they closed the warehouse  several hours so they could reconfigure and squeeze in more bins. Managers hope to add 50 more, although that still won't meet the need, said the  group's executive director, Estela Lopez.\n";
		article += "Bigger changes are expected in coming months. For instance, the warehouse has a rule prohibiting clients from changing clothes at the site. That no longer seems practical, not with mothers bringing their children in to fetch clothes for school. So operators are hoping to add a private dressing area.\n";
		article += "That move would come with complications unthinkable somewhere else. Skid row is home to a large concentration of sex offenders, and precautions would have to be taken. Also, many addicts in the area search each day for a secluded place to shoot up; warehouse supervisor Peggy Washington said she fears they might try to take advantage of a dressing room. \"I don't need anybody dying here,\" she said.\n";
		article += "Still, everyone agrees aggressive steps must be taken. \"There are going to be things we're going to have to talk about that we've never had to talk about before,\" Lopez said.\n";
		article += "The other day, Krystle Marage sifted through her family's bins. She and her daughters -- Mishanta, 14; Jay, 19; and Lilly, 21 -- stop in at least twice a day to retrieve clothes, grab a bar of soap, even snag a pack of Ramen noodles if they need a snack. They've all memorized the numbers assigned to their bins: 194, 202, 287, 348.\n";
		article += "Marage stuffed plastic bags full of dirty clothes into the containers. Soon, she said, it would be laundry day. She rolled her eyes. \"A momma's work,\" she said, \"is never done.\"\n";
		article += "The association's warehouse, along with the district's missions, food banks and social services, is one of the things that make skid row work, in its own tragic way.\n";
		article += "For the homeless, the most mundane steps of the day -- going to the bathroom, finding a shower -- are tiring ordeals. It is particularly difficult for homeless people to figure out what to do with their stuff. After a point, they can't carry it with them, but if they leave it on the street, it'll be lost or stolen. Even if, like Marage's family, they are staying at one of the area's missions, most facilities limit the belongings that can be brought in and offer no storage space.\n";
		article += "That's where Central City East's warehouse comes in, taking care, as Lopez puts it, \"of one tiny aspect of an enormous conundrum.\"\n";
		article += "In 2002, the warehouse was born of tension on the streets, when merchants became concerned about homeless people leaving bedrolls and shopping carts in front of their businesses. A local developer and association board member, Richard Meruelo, donated the warehouse, which is financed by the Los Angeles Homeless Services Authority and local business improvement districts.\n";
		article += "The bins are popular. As long as clients renew them once a week, they can keep them in perpetuity, and many do. Only a handful of empty containers open up each day, and people routinely wait all night to try to qualify for one. Shortly before dawn each day, workers distribute scraps of paper -- \"#1,\" \"#2,\" \"#3\" -- identifying the hopefuls who were first in line.\n";
		article += "Clients keep an astonishing array of items at the warehouse. Some are predictable: blankets for colder nights, rolls of toilet paper, umbrellas. Some are more surprising. One man, wearing a jacket with the words \"God Bless America\" on it, sifted recently through the personal library he stores in his bin, including Jules Verne's \"Journey to the Center of the Earth\" and Alexandre Dumas' \"The King of Romance.\"\n";
		article += "\"I don't know what I'd do without this place,\" said Rick Cuthbertson, 52, a former plumber and electrician who is unemployed and on the streets. \"I would've given up. I would've committed suicide.\" He is not being hyperbolic; a diagnosed schizophrenic, Cuthbertson said he becomes suicidal without his pills, which he stores in his bin.\n";
		article += "Last year, the clientele began to change. A couple living on the sidewalk asked for a bin after having a child. The warehouse is strict about providing just one bin per client, but another woman soon asked for an extra; she was having trouble fitting in her daughter's school clothes. \"These were people who did not belong here,\" Washington said.\n";
		article += "Two weeks ago, Marage walked through the door. She asked a worker for four bins -- one for herself, one for each girl.\n";
		article += "She'd been looking for work for months, Marage said -- as a nanny, an office cleaner, you name it. Her youngest daughter attends Compton High School, but her older daughters had been looking for work too. Nothing. Never did they think they'd wind up here, she said. They  just ran out of money and had nowhere to turn.\n";
		article += "\"We tried to avoid it as long as possible. But bad things happen, and it can happen to anybody,\" she said. \"The things we see here . . . it hurts. And it hurts to know that we're in the same boat as everybody else down here.\"\n";
		article += "The future is bright, she said; she remains convinced of that. \"The Lord said: 'Ain't nothing too tough for me,' \" she said. \"All around here, you see people falling apart, and I can't afford to fall apart.\"\n";
		article += "Still, the transition has been tough on them all. They often have to walk to two different missions to fill up on dinner; that practice is frowned upon, but some missions, hurting financially like everyone else, have begun scaling back their meal portions.\n";
		article += "The experience has been especially hard on Mishanta, the youngest, who hasn't been talking much lately, though she periodically tells her mother: \"I can't take it anymore.\"\n";
		article += "Mishy, as her sisters call her, is a talented artist. This week, a friend at school gave her an early birthday present, a clock decorated with Japanese-style anime characters.\n";
		article += "It was supposed to go on the wall, but Mishy has nowhere to hang it. So she put it in her bin, No. 287. It's still in there, hidden away in a room that never seems to get warm, under roosting pigeons and yellowing rolls of flypaper.\n";
		article += "scott.gold@latimes.com";
				
		NewsDocument doc = extractor.parseArticle();
		
		assertEquals(article, doc.getArticle());
	}

}
