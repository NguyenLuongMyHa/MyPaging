# MyPaging (Paging 3 - Android Jetpack)
This repo follow [Android Code Lab](https://codelabs.developers.google.com/codelabs/android-paging/#0).  

Data pagination is the process of breaking down a larger set of objects into smaller chunks, commonly referred to as pages. Typically, paging applies to the results of search queries when the server can return too many objects at once.
Because smaller data pages can be returned to client applications much faster, client users don't need to wait. As a result, the user experience of the app is significantly improved, which is the main advantage of paging.
* Handles in-memory cache.
* Requests data when the user is close to the end of the list.
```
Component:
```
* PagingData - a container for paginated data. Each refresh of data will have a separate corresponding PagingData.
* PagingSource - a PagingSource is the base class for loading snapshots of data into a stream of PagingData.
* Pager.flow - builds a Flow<PagingData>, based on a PagingConfig and a function that defines how to construct the implemented PagingSource.
* PagingDataAdapter - a RecyclerView.Adapter that presents PagingData in a RecyclerView. The PagingDataAdapter can be connected to a Kotlin Flow, a LiveData, an RxJava Flowable, or an RxJava Observable. The PagingDataAdapter listens to internal PagingData loading events as pages are loaded and uses DiffUtil on a background thread to compute fine-grained updates as updated content is received in the form of new PagingData objects.
* RemoteMediator - helps implement pagination from network and database.
