/**
 * Package contains several types of services.
 * 
 * The services are:
 * <ul>
 * <li>Client*Impl – services used by client part of application to communicate
 * with server side,
 * <li>services accessing database and providing abstraction of its operations,
 * <li>RecommendationService – calculates recommendation,
 * <li>CrawlerService – initiates crawling,
 * <li>BackupService – backs up web page
 * <li>*Servlet – either public (any user can call them) or internal (platform
 * calls them),
 * <li>*Task – tasks inserted to Task Queue to delay their execution.
 * </ul>
 */
package cz.artique.server.service;