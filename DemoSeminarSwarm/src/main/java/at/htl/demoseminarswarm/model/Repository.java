package at.htl.demoseminarswarm.model;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;

@ApplicationScoped
public class Repository {

    JsonArray repository;

    public JsonArray getRepository() {
        return repository;
    }

    public JsonObject getPersonAtIndex(int index) {
        if (index > repository.size() - 1) {
            return null;
        }
        return repository.getJsonObject(index);
    }

    public JsonObject getFirst() {
        return repository.getJsonObject(0);
    }

    public JsonObject getLast() {
        return repository.getJsonObject(repository.size() - 1);
    }

    public int add(JsonObject person) {

        if (!person.containsKey("id")) {
            person = addIdToJsonObject(person, nextId());
        }
        repository.add(person);
        int index = repository.indexOf(person);
        return index;

    }

    public void delete(int index) {
        repository.remove(index);
    }

    public int update(JsonObject person) {
        int id = person.getInt("id");
        for (JsonValue personJson : repository) {
            JsonObject p = ((JsonObject) personJson);
            if (p.getInt("id") == person.getInt("id")) {
                int index = repository.indexOf(personJson);
                repository.remove(index);
                repository.add(index, p);
                return index;
            }
        }
        return -1;
    }

//    public boolean addAtIndex(int index, JsonObject person) {
//        return repository.add(index, person.);
//    }

    // https://rmannibucau.wordpress.com/2015/03/10/cdi-and-startup/
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        System.out.println("********** PostConstruct ***************");
        initRepo();
        for (JsonValue jsonValue : repository) {
            System.out.println(jsonValue.toString());
        }
    }

//    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
//        repository.clear();
//    }

    /**
     * Da jedes JsonObject immutable ist, muss es neu erstellt werden, wenn ein neues Feld hinzukommt
     * <p>
     * <p>
     * // http://stackoverflow.com/questions/26346060/javax-json-add-new-jsonnumber-to-existing-jsonobject
     *
     * @param jo
     * @param id
     * @return
     */
    private JsonObject addIdToJsonObject(JsonObject jo, int id) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        for (Map.Entry<String, JsonValue> entry : jo.entrySet()) {
            job.add(entry.getKey(), entry.getValue());
        }

        job.add("id", id).build();
        return job.build();
    }

    private int nextId() {
        int maxId = -1;
        for (JsonValue person : repository) {
            JsonObject jsonPerson = (JsonObject) person;
            if (jsonPerson.containsKey("id")) {
                if (jsonPerson.getInt("id") > maxId) {
                    maxId = jsonPerson.getInt("id");
                }
            }
        }
        return maxId;
    }

    private void initRepo() {
        String personData = "[{\"id\":1,\"gender\":\"M\",\"firstname\":\"Fred\",\"lastname\":\"Perez\",\"email\":\"fperez0@google.com.br\",\"country\":\"Finland\",\"age\":32,\"registered\":false}," +
                "{\"id\":2,\"gender\":\"F\",\"firstname\":\"Phyllis\",\"lastname\":\"Boyd\",\"email\":\"pboyd1@youtube.com\",\"country\":\"Sweden\",\"age\":26,\"registered\":false}," +
                "{\"id\":3,\"gender\":\"M\",\"firstname\":\"Johnny\",\"lastname\":\"Kelly\",\"email\":\"jkelly2@answers.com\",\"country\":\"Philippines\",\"age\":76,\"registered\":false}," +
                "{\"id\":4,\"gender\":\"F\",\"firstname\":\"Betty\",\"lastname\":\"Torres\",\"email\":\"btorres3@github.io\",\"country\":\"China\",\"age\":38,\"registered\":false}," +
                "{\"id\":5,\"gender\":\"F\",\"firstname\":\"Evelyn\",\"lastname\":\"Coleman\",\"email\":\"ecoleman4@foxnews.com\",\"country\":\"Philippines\",\"age\":76,\"registered\":false}," +
                "{\"id\":6,\"gender\":\"M\",\"firstname\":\"Michael\",\"lastname\":\"Williams\",\"email\":\"mwilliams5@bloglovin.com\",\"country\":\"Indonesia\",\"age\":72,\"registered\":false}," +
                "{\"id\":7,\"gender\":\"M\",\"firstname\":\"Jason\",\"lastname\":\"Lewis\",\"email\":\"jlewis6@reference.com\",\"country\":\"Vietnam\",\"age\":48,\"registered\":false}," +
                "{\"id\":8,\"gender\":\"F\",\"firstname\":\"Judy\",\"lastname\":\"Moore\",\"email\":\"jmoore7@bravesites.com\",\"country\":\"Germany\",\"age\":34,\"registered\":false}," +
                "{\"id\":9,\"gender\":\"M\",\"firstname\":\"Gregory\",\"lastname\":\"Green\",\"email\":\"ggreen8@over-blog.com\",\"country\":\"Brazil\",\"age\":71,\"registered\":false}," +
                "{\"id\":10,\"gender\":\"F\",\"firstname\":\"Andrea\",\"lastname\":\"Howard\",\"email\":\"ahoward9@jiathis.com\",\"country\":\"United States\",\"age\":55,\"registered\":false}," +
                "{\"id\":11,\"gender\":\"M\",\"firstname\":\"Frank\",\"lastname\":\"Williams\",\"email\":\"fwilliamsa@yale.edu\",\"country\":\"Morocco\",\"age\":77,\"registered\":false}," +
                "{\"id\":12,\"gender\":\"M\",\"firstname\":\"Gregory\",\"lastname\":\"Garcia\",\"email\":\"ggarciab@squarespace.com\",\"country\":\"China\",\"age\":73,\"registered\":false}," +
                "{\"id\":13,\"gender\":\"M\",\"firstname\":\"Philip\",\"lastname\":\"Hill\",\"email\":\"phillc@bbb.org\",\"country\":\"Portugal\",\"age\":65,\"registered\":false}," +
                "{\"id\":14,\"gender\":\"M\",\"firstname\":\"Keith\",\"lastname\":\"Tucker\",\"email\":\"ktuckerd@sohu.com\",\"country\":\"China\",\"age\":79,\"registered\":false}," +
                "{\"id\":15,\"gender\":\"F\",\"firstname\":\"Marie\",\"lastname\":\"Patterson\",\"email\":\"mpattersone@nationalgeographic.com\",\"country\":\"Kuwait\",\"age\":50,\"registered\":false}," +
                "{\"id\":16,\"gender\":\"M\",\"firstname\":\"Gerald\",\"lastname\":\"Robertson\",\"email\":\"grobertsonf@marketwatch.com\",\"country\":\"Sweden\",\"age\":41,\"registered\":false}," +
                "{\"id\":17,\"gender\":\"F\",\"firstname\":\"Phyllis\",\"lastname\":\"Hughes\",\"email\":\"phughesg@theatlantic.com\",\"country\":\"Poland\",\"age\":41,\"registered\":false}," +
                "{\"id\":18,\"gender\":\"M\",\"firstname\":\"Ronald\",\"lastname\":\"Morris\",\"email\":\"rmorrish@stumbleupon.com\",\"country\":\"Philippines\",\"age\":74,\"registered\":false}," +
                "{\"id\":19,\"gender\":\"F\",\"firstname\":\"Lois\",\"lastname\":\"Chavez\",\"email\":\"lchavezi@opensource.org\",\"country\":\"Brazil\",\"age\":47,\"registered\":false}," +
                "{\"id\":20,\"gender\":\"M\",\"firstname\":\"James\",\"lastname\":\"Campbell\",\"email\":\"jcampbellj@seattletimes.com\",\"country\":\"France\",\"age\":53,\"registered\":false}," +
                "{\"id\":21,\"gender\":\"M\",\"firstname\":\"James\",\"lastname\":\"Adams\",\"email\":\"jadamsk@harvard.edu\",\"country\":\"China\",\"age\":64,\"registered\":false}," +
                "{\"id\":22,\"gender\":\"F\",\"firstname\":\"Stephanie\",\"lastname\":\"Carroll\",\"email\":\"scarrolll@hugedomains.com\",\"country\":\"Indonesia\",\"age\":44,\"registered\":false}," +
                "{\"id\":23,\"gender\":\"F\",\"firstname\":\"Martha\",\"lastname\":\"Simpson\",\"email\":\"msimpsonm@berkeley.edu\",\"country\":\"China\",\"age\":44,\"registered\":false}," +
                "{\"id\":24,\"gender\":\"M\",\"firstname\":\"Antonio\",\"lastname\":\"Bishop\",\"email\":\"abishopn@digg.com\",\"country\":\"Central African Republic\",\"age\":27,\"registered\":false}," +
                "{\"id\":25,\"gender\":\"M\",\"firstname\":\"Scott\",\"lastname\":\"Dean\",\"email\":\"sdeano@techcrunch.com\",\"country\":\"China\",\"age\":57,\"registered\":false}," +
                "{\"id\":26,\"gender\":\"F\",\"firstname\":\"Sarah\",\"lastname\":\"Fowler\",\"email\":\"sfowlerp@army.mil\",\"country\":\"Tanzania\",\"age\":20,\"registered\":false}," +
                "{\"id\":27,\"gender\":\"F\",\"firstname\":\"Lisa\",\"lastname\":\"Romero\",\"email\":\"lromeroq@va.gov\",\"country\":\"United States\",\"age\":67,\"registered\":false}," +
                "{\"id\":28,\"gender\":\"M\",\"firstname\":\"John\",\"lastname\":\"Ramirez\",\"email\":\"jramirezr@amazonaws.com\",\"country\":\"Greenland\",\"age\":27,\"registered\":false}," +
                "{\"id\":29,\"gender\":\"F\",\"firstname\":\"Amy\",\"lastname\":\"Thompson\",\"email\":\"athompsons@sina.com.cn\",\"country\":\"Thailand\",\"age\":31,\"registered\":false}," +
                "{\"id\":30,\"gender\":\"M\",\"firstname\":\"William\",\"lastname\":\"Gordon\",\"email\":\"wgordont@bloomberg.com\",\"country\":\"Colombia\",\"age\":27,\"registered\":false}," +
                "{\"id\":31,\"gender\":\"F\",\"firstname\":\"Andrea\",\"lastname\":\"Kelly\",\"email\":\"akellyu@opensource.org\",\"country\":\"Russia\",\"age\":74,\"registered\":false}," +
                "{\"id\":32,\"gender\":\"M\",\"firstname\":\"Ernest\",\"lastname\":\"Burns\",\"email\":\"eburnsv@youtube.com\",\"country\":\"China\",\"age\":39,\"registered\":false}," +
                "{\"id\":33,\"gender\":\"F\",\"firstname\":\"Robin\",\"lastname\":\"Bowman\",\"email\":\"rbowmanw@behance.net\",\"country\":\"Burundi\",\"age\":77,\"registered\":false}," +
                "{\"id\":34,\"gender\":\"F\",\"firstname\":\"Denise\",\"lastname\":\"Peters\",\"email\":\"dpetersx@trellian.com\",\"country\":\"Colombia\",\"age\":39,\"registered\":false}," +
                "{\"id\":35,\"gender\":\"F\",\"firstname\":\"Irene\",\"lastname\":\"White\",\"email\":\"iwhitey@com.com\",\"country\":\"Cuba\",\"age\":48,\"registered\":false}," +
                "{\"id\":36,\"gender\":\"F\",\"firstname\":\"Jean\",\"lastname\":\"Webb\",\"email\":\"jwebbz@miibeian.gov.cn\",\"country\":\"Bulgaria\",\"age\":72,\"registered\":false}," +
                "{\"id\":37,\"gender\":\"F\",\"firstname\":\"Lois\",\"lastname\":\"Martinez\",\"email\":\"lmartinez10@irs.gov\",\"country\":\"Poland\",\"age\":79,\"registered\":false}," +
                "{\"id\":38,\"gender\":\"M\",\"firstname\":\"Bobby\",\"lastname\":\"Mason\",\"email\":\"bmason11@jigsy.com\",\"country\":\"Indonesia\",\"age\":33,\"registered\":false}," +
                "{\"id\":39,\"gender\":\"F\",\"firstname\":\"Cheryl\",\"lastname\":\"Carter\",\"email\":\"ccarter12@zimbio.com\",\"country\":\"Yemen\",\"age\":57,\"registered\":false}," +
                "{\"id\":40,\"gender\":\"F\",\"firstname\":\"Angela\",\"lastname\":\"Richardson\",\"email\":\"arichardson13@blog.com\",\"country\":\"Sweden\",\"age\":37,\"registered\":false}," +
                "{\"id\":41,\"gender\":\"M\",\"firstname\":\"Gregory\",\"lastname\":\"Pierce\",\"email\":\"gpierce14@artisteer.com\",\"country\":\"Argentina\",\"age\":26,\"registered\":false}," +
                "{\"id\":42,\"gender\":\"M\",\"firstname\":\"Carlos\",\"lastname\":\"Austin\",\"email\":\"caustin15@wsj.com\",\"country\":\"Indonesia\",\"age\":53,\"registered\":false}," +
                "{\"id\":43,\"gender\":\"M\",\"firstname\":\"Anthony\",\"lastname\":\"Austin\",\"email\":\"aaustin16@jalbum.net\",\"country\":\"Uzbekistan\",\"age\":29,\"registered\":false}," +
                "{\"id\":44,\"gender\":\"F\",\"firstname\":\"Kelly\",\"lastname\":\"Garrett\",\"email\":\"kgarrett17@marriott.com\",\"country\":\"Ukraine\",\"age\":58,\"registered\":false}," +
                "{\"id\":45,\"gender\":\"F\",\"firstname\":\"Cheryl\",\"lastname\":\"Spencer\",\"email\":\"cspencer18@weibo.com\",\"country\":\"Sri Lanka\",\"age\":20,\"registered\":false}," +
                "{\"id\":46,\"gender\":\"F\",\"firstname\":\"Julie\",\"lastname\":\"Elliott\",\"email\":\"jelliott19@state.tx.us\",\"country\":\"Indonesia\",\"age\":36,\"registered\":false}," +
                "{\"id\":47,\"gender\":\"M\",\"firstname\":\"Albert\",\"lastname\":\"Ryan\",\"email\":\"aryan1a@columbia.edu\",\"country\":\"Greece\",\"age\":39,\"registered\":false}," +
                "{\"id\":48,\"gender\":\"M\",\"firstname\":\"Jose\",\"lastname\":\"Sims\",\"email\":\"jsims1b@example.com\",\"country\":\"Indonesia\",\"age\":40,\"registered\":false}," +
                "{\"id\":49,\"gender\":\"M\",\"firstname\":\"Steve\",\"lastname\":\"Matthews\",\"email\":\"smatthews1c@prweb.com\",\"country\":\"Philippines\",\"age\":70,\"registered\":false}," +
                "{\"id\":50,\"gender\":\"F\",\"firstname\":\"Robin\",\"lastname\":\"Hill\",\"email\":\"rhill1d@blogs.com\",\"country\":\"South Africa\",\"age\":66,\"registered\":false}," +
                "{\"id\":51,\"gender\":\"F\",\"firstname\":\"Carolyn\",\"lastname\":\"Perry\",\"email\":\"cperry1e@geocities.com\",\"country\":\"Philippines\",\"age\":57,\"registered\":false}," +
                "{\"id\":52,\"gender\":\"M\",\"firstname\":\"Frank\",\"lastname\":\"Little\",\"email\":\"flittle1f@jalbum.net\",\"country\":\"China\",\"age\":41,\"registered\":false}," +
                "{\"id\":53,\"gender\":\"F\",\"firstname\":\"Nicole\",\"lastname\":\"Andrews\",\"email\":\"nandrews1g@elpais.com\",\"country\":\"Czech Republic\",\"age\":51,\"registered\":false}," +
                "{\"id\":54,\"gender\":\"M\",\"firstname\":\"Bobby\",\"lastname\":\"Peters\",\"email\":\"bpeters1h@hugedomains.com\",\"country\":\"Kazakhstan\",\"age\":75,\"registered\":false}," +
                "{\"id\":55,\"gender\":\"M\",\"firstname\":\"Mark\",\"lastname\":\"Fox\",\"email\":\"mfox1i@studiopress.com\",\"country\":\"Russia\",\"age\":53,\"registered\":false}," +
                "{\"id\":56,\"gender\":\"F\",\"firstname\":\"Joyce\",\"lastname\":\"Lawrence\",\"email\":\"jlawrence1j@smugmug.com\",\"country\":\"United States\",\"age\":21,\"registered\":false}," +
                "{\"id\":57,\"gender\":\"F\",\"firstname\":\"Anna\",\"lastname\":\"Porter\",\"email\":\"aporter1k@sohu.com\",\"country\":\"Chile\",\"age\":57,\"registered\":false}," +
                "{\"id\":58,\"gender\":\"F\",\"firstname\":\"Teresa\",\"lastname\":\"Woods\",\"email\":\"twoods1l@storify.com\",\"country\":\"Indonesia\",\"age\":39,\"registered\":false}," +
                "{\"id\":59,\"gender\":\"F\",\"firstname\":\"Diane\",\"lastname\":\"Gardner\",\"email\":\"dgardner1m@flickr.com\",\"country\":\"Iran\",\"age\":70,\"registered\":false}," +
                "{\"id\":60,\"gender\":\"F\",\"firstname\":\"Lillian\",\"lastname\":\"Richards\",\"email\":\"lrichards1n@arstechnica.com\",\"country\":\"Zambia\",\"age\":71,\"registered\":false}," +
                "{\"id\":61,\"gender\":\"M\",\"firstname\":\"George\",\"lastname\":\"Hart\",\"email\":\"ghart1o@unblog.fr\",\"country\":\"Mongolia\",\"age\":71,\"registered\":false}," +
                "{\"id\":62,\"gender\":\"M\",\"firstname\":\"Richard\",\"lastname\":\"Sims\",\"email\":\"rsims1p@desdev.cn\",\"country\":\"Philippines\",\"age\":21,\"registered\":false}," +
                "{\"id\":63,\"gender\":\"F\",\"firstname\":\"Betty\",\"lastname\":\"Burke\",\"email\":\"bburke1q@forbes.com\",\"country\":\"New Caledonia\",\"age\":36,\"registered\":false}," +
                "{\"id\":64,\"gender\":\"M\",\"firstname\":\"Lawrence\",\"lastname\":\"Bradley\",\"email\":\"lbradley1r@hatena.ne.jp\",\"country\":\"Russia\",\"age\":72,\"registered\":false}," +
                "{\"id\":65,\"gender\":\"F\",\"firstname\":\"Joan\",\"lastname\":\"Gray\",\"email\":\"jgray1s@hc360.com\",\"country\":\"Indonesia\",\"age\":28,\"registered\":false}," +
                "{\"id\":66,\"gender\":\"F\",\"firstname\":\"Susan\",\"lastname\":\"Coleman\",\"email\":\"scoleman1t@wordpress.org\",\"country\":\"Kazakhstan\",\"age\":75,\"registered\":false}," +
                "{\"id\":67,\"gender\":\"M\",\"firstname\":\"Dennis\",\"lastname\":\"Ellis\",\"email\":\"dellis1u@bing.com\",\"country\":\"Philippines\",\"age\":20,\"registered\":false}," +
                "{\"id\":68,\"gender\":\"M\",\"firstname\":\"Joseph\",\"lastname\":\"Robertson\",\"email\":\"jrobertson1v@stumbleupon.com\",\"country\":\"Russia\",\"age\":39,\"registered\":false}," +
                "{\"id\":69,\"gender\":\"M\",\"firstname\":\"Benjamin\",\"lastname\":\"Campbell\",\"email\":\"bcampbell1w@google.ca\",\"country\":\"Guatemala\",\"age\":65,\"registered\":false}," +
                "{\"id\":70,\"gender\":\"M\",\"firstname\":\"Robert\",\"lastname\":\"Lee\",\"email\":\"rlee1x@slate.com\",\"country\":\"South Africa\",\"age\":67,\"registered\":false}," +
                "{\"id\":71,\"gender\":\"M\",\"firstname\":\"Samuel\",\"lastname\":\"Jacobs\",\"email\":\"sjacobs1y@prlog.org\",\"country\":\"Israel\",\"age\":76,\"registered\":false}," +
                "{\"id\":72,\"gender\":\"F\",\"firstname\":\"Jane\",\"lastname\":\"Simpson\",\"email\":\"jsimpson1z@sbwire.com\",\"country\":\"Ukraine\",\"age\":35,\"registered\":false}," +
                "{\"id\":73,\"gender\":\"F\",\"firstname\":\"Jessica\",\"lastname\":\"Simmons\",\"email\":\"jsimmons20@shop-pro.jp\",\"country\":\"Japan\",\"age\":57,\"registered\":false}," +
                "{\"id\":74,\"gender\":\"F\",\"firstname\":\"Debra\",\"lastname\":\"Daniels\",\"email\":\"ddaniels21@springer.com\",\"country\":\"Philippines\",\"age\":53,\"registered\":false}," +
                "{\"id\":75,\"gender\":\"F\",\"firstname\":\"Angela\",\"lastname\":\"Rivera\",\"email\":\"arivera22@who.int\",\"country\":\"China\",\"age\":38,\"registered\":false}," +
                "{\"id\":76,\"gender\":\"F\",\"firstname\":\"Theresa\",\"lastname\":\"White\",\"email\":\"twhite23@google.com.au\",\"country\":\"Russia\",\"age\":31,\"registered\":false}," +
                "{\"id\":77,\"gender\":\"F\",\"firstname\":\"Joyce\",\"lastname\":\"Ross\",\"email\":\"jross24@so-net.ne.jp\",\"country\":\"Russia\",\"age\":24,\"registered\":false}," +
                "{\"id\":78,\"gender\":\"F\",\"firstname\":\"Mildred\",\"lastname\":\"Matthews\",\"email\":\"mmatthews25@issuu.com\",\"country\":\"Ukraine\",\"age\":58,\"registered\":false}," +
                "{\"id\":79,\"gender\":\"F\",\"firstname\":\"Pamela\",\"lastname\":\"Boyd\",\"email\":\"pboyd26@amazonaws.com\",\"country\":\"Mexico\",\"age\":69,\"registered\":false}," +
                "{\"id\":80,\"gender\":\"F\",\"firstname\":\"Cynthia\",\"lastname\":\"Dunn\",\"email\":\"cdunn27@upenn.edu\",\"country\":\"Philippines\",\"age\":57,\"registered\":false}," +
                "{\"id\":81,\"gender\":\"F\",\"firstname\":\"Rose\",\"lastname\":\"Elliott\",\"email\":\"relliott28@hexun.com\",\"country\":\"Poland\",\"age\":74,\"registered\":false}," +
                "{\"id\":82,\"gender\":\"F\",\"firstname\":\"Joan\",\"lastname\":\"Thompson\",\"email\":\"jthompson29@hp.com\",\"country\":\"Zimbabwe\",\"age\":37,\"registered\":false}," +
                "{\"id\":83,\"gender\":\"M\",\"firstname\":\"Gerald\",\"lastname\":\"Bishop\",\"email\":\"gbishop2a@t.co\",\"country\":\"Argentina\",\"age\":50,\"registered\":false}," +
                "{\"id\":84,\"gender\":\"M\",\"firstname\":\"Russell\",\"lastname\":\"Payne\",\"email\":\"rpayne2b@drupal.org\",\"country\":\"China\",\"age\":69,\"registered\":false}," +
                "{\"id\":85,\"gender\":\"M\",\"firstname\":\"Philip\",\"lastname\":\"Burns\",\"email\":\"pburns2c@vistaprint.com\",\"country\":\"Canada\",\"age\":52,\"registered\":false}," +
                "{\"id\":86,\"gender\":\"M\",\"firstname\":\"Fred\",\"lastname\":\"Rogers\",\"email\":\"frogers2d@amazon.de\",\"country\":\"China\",\"age\":28,\"registered\":false}," +
                "{\"id\":87,\"gender\":\"F\",\"firstname\":\"Amanda\",\"lastname\":\"Morgan\",\"email\":\"amorgan2e@feedburner.com\",\"country\":\"United States\",\"age\":66,\"registered\":false}," +
                "{\"id\":88,\"gender\":\"F\",\"firstname\":\"Kathleen\",\"lastname\":\"Vasquez\",\"email\":\"kvasquez2f@sciencedirect.com\",\"country\":\"Albania\",\"age\":24,\"registered\":false}," +
                "{\"id\":89,\"gender\":\"F\",\"firstname\":\"Theresa\",\"lastname\":\"Simpson\",\"email\":\"tsimpson2g@nyu.edu\",\"country\":\"Russia\",\"age\":56,\"registered\":false}," +
                "{\"id\":90,\"gender\":\"F\",\"firstname\":\"Helen\",\"lastname\":\"Dixon\",\"email\":\"hdixon2h@acquirethisname.com\",\"country\":\"China\",\"age\":52,\"registered\":false}," +
                "{\"id\":91,\"gender\":\"F\",\"firstname\":\"Rachel\",\"lastname\":\"Oliver\",\"email\":\"roliver2i@unblog.fr\",\"country\":\"Panama\",\"age\":27,\"registered\":false}," +
                "{\"id\":92,\"gender\":\"M\",\"firstname\":\"Jerry\",\"lastname\":\"Martin\",\"email\":\"jmartin2j@buzzfeed.com\",\"country\":\"Indonesia\",\"age\":60,\"registered\":false}," +
                "{\"id\":93,\"gender\":\"M\",\"firstname\":\"George\",\"lastname\":\"Cruz\",\"email\":\"gcruz2k@taobao.com\",\"country\":\"Philippines\",\"age\":77,\"registered\":false}," +
                "{\"id\":94,\"gender\":\"F\",\"firstname\":\"Rachel\",\"lastname\":\"Jacobs\",\"email\":\"rjacobs2l@nhs.uk\",\"country\":\"United States\",\"age\":42,\"registered\":false}," +
                "{\"id\":95,\"gender\":\"M\",\"firstname\":\"Brian\",\"lastname\":\"Cole\",\"email\":\"bcole2m@artisteer.com\",\"country\":\"Indonesia\",\"age\":67,\"registered\":false}," +
                "{\"id\":96,\"gender\":\"M\",\"firstname\":\"Harry\",\"lastname\":\"Mills\",\"email\":\"hmills2n@jiathis.com\",\"country\":\"Brazil\",\"age\":46,\"registered\":false}," +
                "{\"id\":97,\"gender\":\"F\",\"firstname\":\"Bonnie\",\"lastname\":\"Little\",\"email\":\"blittle2o@disqus.com\",\"country\":\"Uzbekistan\",\"age\":38,\"registered\":false}," +
                "{\"id\":98,\"gender\":\"F\",\"firstname\":\"Heather\",\"lastname\":\"Kelly\",\"email\":\"hkelly2p@joomla.org\",\"country\":\"Czech Republic\",\"age\":40,\"registered\":false}," +
                "{\"id\":99,\"gender\":\"M\",\"firstname\":\"Raymond\",\"lastname\":\"Andrews\",\"email\":\"randrews2q@vimeo.com\",\"country\":\"Indonesia\",\"age\":22,\"registered\":false}," +
                "{\"id\":100,\"gender\":\"M\",\"firstname\":\"Nicholas\",\"lastname\":\"Dean\",\"email\":\"ndean2r@sfgate.com\",\"country\":\"Turkmenistan\",\"age\":58,\"registered\":true}]";

        JsonReader jsonReader = Json.createReader(new StringReader(personData));
        repository = jsonReader.readArray();
    }
}
