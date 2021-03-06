package spray

import com.typesafe.config.Config
import akka.actor.ActorSystem

/**
 * Provides a simple Solr DSL using [[http://akka.io/ Akka]], 
 * [[http://spray.io/documentation/spray-client/ spray-client]]
 * and [[http://github.com/spray/spray-json spray-json]].
 * 
 * ==Basic Flow==
 * 
 *  - Construct query using the [[spray.solr.Solr]] object,
 *  - send the query to the (running) [[spray.solr.SolrService]] actor,
 *  - handle the resulting SolrServiceResponse by providing an implicit JsonFormat.
 * 
 * ==Example==
 * 
 * ===Setup===
 * Create SolrService actor:
 * {{{
 * val solrService = system.actorOf(
 *		props = Props[SolrService],
 *		name = "solr-service");
 * }}}
 * 
 * Don't forget that you need to have a spray-client 
 * running at '''context.actorFor("../http-client")'''
 * (relative to the context of the SolrService).
 * 
 * ===Query===
 * Create query "http://localhost:8983/solr/core1/select?q=test&wt=json&rows=10" with:
 * {{{
 * val solrQuery = Solr("localhost", 8983, "core1").q("test").rows(10)()
 * }}}
 * or if you have com.typesafe.config.Config config file with something like
 * {{{
 * test.solr {
 * 	ip = "localhost"
 *  	port = 8983
 *  	core = "/solr/core1"
 * }
 * }}}
 * and an akka.actor.ActorSystem with that config in scope:
 * {{{
 * val solrQuery = Solr("test.solr").q("test").rows(10)()
 * }}}
 * 
 * Then send the request off to the SolrService instance 
 * (let's say we have it in scope in '''solrService''')
 * and remember the the future (if doing ask):
 * {{{
 * val solrResponseFuture = solrService ? solrQuery
 * }}}
 * 
 * Now say you expect the response from Solr to have 
 * two fields test1 and test2 with string values and
 * you want those end up in a nice case class like:
 * {{{
 * case class SolrTestResult(val test1: String, val test2: String)
 * }}}
 * 
 * You need to get a spray-json formatter for SolrTestResult into scope.
 * You can find out how to do that in the [[http://github.com/spray/spray-json spray-json documentation]].
 * 
 * One you have that you can get the list of results:
 * {{{
 * solrResponseFuture onSuccess {
 * 	case solrResponse: SolrResults => {
 * 		val testResults: List[SolrTestResult] = solrResponse.as[SolrTestResult]
 * 		// do something with the results
 * 	}
 * 	case SolrError => {
 * 		// handle the error
 *  }
 * }
 * }}}
 */
package object solr {

}