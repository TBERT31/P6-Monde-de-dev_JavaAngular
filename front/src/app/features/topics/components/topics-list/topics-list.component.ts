import { Component, OnInit, OnDestroy } from '@angular/core';
import { Topic } from '../../interfaces/topic.interface';
import { Observable, Subscription } from 'rxjs';
import { TopicService } from '../../services/topic.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-topics-list',
  templateUrl: './topics-list.component.html',
  styleUrls: ['./topics-list.component.scss']
})
export class TopicsListComponent implements OnInit, OnDestroy {
  public topics$: Observable<Topic[]>;
  private userId: number | undefined;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private topicService: TopicService,
    private sessionService: SessionService
  ) {
    this.topics$ = this.topicService.getAllTopics();
  }

  ngOnInit(): void {
    const session = this.sessionService.getSession();
    if (session) {
      this.userId = session.id;
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  public isSubscribed(topic: Topic): boolean {
    return topic.users_subscribed.includes(this.userId!);
  }

  public subscribe(topic: Topic): void {
    if (this.userId !== undefined) {
      const sub = this.topicService.subscribeUserToTopic(topic.id, this.userId).subscribe(() => {
        topic.users_subscribed.push(this.userId!);
      });
      this.subscriptions.add(sub);
    }
  }

  public unsubscribe(topic: Topic): void {
    if (this.userId !== undefined) {
      const sub = this.topicService.unsubscribeUserToTopic(topic.id, this.userId).subscribe(() => {
        const index = topic.users_subscribed.indexOf(this.userId!);
        if (index > -1) {
          topic.users_subscribed.splice(index, 1);
        }
      });
      this.subscriptions.add(sub);
    }
  }
}
